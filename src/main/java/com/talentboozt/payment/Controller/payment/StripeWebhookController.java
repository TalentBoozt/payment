package com.talentboozt.payment.Controller.payment;

import com.talentboozt.payment.Model.payment.BillingHistoryModel;
import com.talentboozt.payment.Model.payment.InvoicesModel;
import com.talentboozt.payment.Model.payment.PaymentMethodsModel;
import com.talentboozt.payment.Model.payment.SubscriptionsModel;
import com.talentboozt.payment.Repository.payment.BillingHistoryRepository;
import com.talentboozt.payment.Repository.payment.InvoiceRepository;
import com.talentboozt.payment.Repository.payment.PaymentMethodRepository;
import com.talentboozt.payment.Service.CmpPostedJobsService;
import com.talentboozt.payment.Service.CompanyService;
import com.talentboozt.payment.Service.CredentialsService;
import com.talentboozt.payment.Service.payment.*;
import com.talentboozt.payment.Utils.ConfigUtility;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    @Autowired
    ConfigUtility configUtility;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Autowired
    BillingHistoryRepository billingHistoryRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    StripeService stripeService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private BillingHistoryService billingHistoryService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CmpPostedJobsService cmpPostedJobsService;

    @Autowired
    private PrePaymentService prePaymentService;

    @PostMapping("/stripe-events")
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload) {
        Event event = Event.GSON.fromJson(payload, Event.class);
        return ResponseEntity.ok("Event processed");
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody Map<String, Object> data) throws StripeException {
        String companyId = (String) data.get("companyId");
        String planName = (String) data.get("planName");

        if (companyId == null || companyId.isEmpty()) {
            throw new IllegalArgumentException("Company ID is required");
        }
        if (planName == null || planName.isEmpty()) {
            throw new IllegalArgumentException("Plan name is required");
        }

        Session session = stripeService.createCheckoutSession(companyId, planName);

        Map<String, String> response = new HashMap<>();
        response.put("id", session.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, configUtility.getProperty("STRIPE_WEBHOOK_SECRET"));
            System.out.println("Received event: " + event.getType());
            switch (event.getType()) {
                case "checkout.session.completed" -> handleCheckoutSessionCompleted(event);
                case "invoice.payment_succeeded" -> handleInvoicePaymentSucceeded(event);
                case "invoice.payment_failed" -> handleInvoicePaymentFailed(event);
                case "invoice.created" -> handleInvoiceCreated(event);
                case "invoice.updated" -> handleInvoiceUpdated(event);
                case "customer.subscription.updated" -> handleSubscriptionUpdated(event);
                case "customer.subscription.deleted" -> handleSubscriptionDeleted(event);
                default -> System.out.println("Unhandled event type: " + event.getType());
            }

            return ResponseEntity.ok("Webhook processed");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Webhook error: " + e.getMessage());
        }
    }

    private void handleCheckoutSessionCompleted(Event event) throws StripeException {
        Session session = (Session) event.getDataObjectDeserializer().getObject().get();
        String companyId = session.getMetadata().get("company_id");

        PaymentIntent paymentIntent = PaymentIntent.retrieve(session.getPaymentIntent());
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentIntent.getPaymentMethod());
        Subscription subscription = Subscription.retrieve(session.getSubscription());
        if (subscription == null) { System.out.println("Subscription is null."); return; }
        if (companyId == null) { System.out.println("Company ID is null."); return; }
        if (paymentMethod == null) { System.out.println("PaymentMethod is null."); return; }

        // Save payment method
        PaymentMethodsModel paymentMethodModel = new PaymentMethodsModel();
        paymentMethodModel.setCompanyId(companyId);
        paymentMethodModel.setType(paymentMethod.getType());
        paymentMethodModel.setLast_four(paymentMethod.getCard().getLast4());
        paymentMethodModel.setExpiry_date(paymentMethod.getCard().getExpMonth() + "/" + paymentMethod.getCard().getExpYear());
        paymentMethodService.save(paymentMethodModel);

        // Save subscription details
        SubscriptionsModel subscriptionsModel = new SubscriptionsModel();
        subscriptionsModel.setCompanyId(companyId);
        subscriptionsModel.setPlan_name(subscription.getItems().getData().get(0).getPlan().getNickname());
        subscriptionsModel.setCost(String.valueOf(subscription.getItems().getData().get(0).getPlan().getAmount() / 100.0));
        subscriptionsModel.setBilling_cycle(subscription.getBillingCycleAnchor().toString());
        subscriptionsModel.setStart_date(subscription.getCurrentPeriodStart().toString());
        subscriptionsModel.setEnd_date(subscription.getCurrentPeriodEnd().toString());
        subscriptionsModel.set_active(true);
        subscriptionService.updateSubscription(companyId, subscriptionsModel);

        // Save billing history
        BillingHistoryModel billingHistory = new BillingHistoryModel();
        billingHistory.setCompanyId(companyId);
        billingHistory.setAmount(String.valueOf(paymentIntent.getAmountReceived()));
        billingHistory.setDate(new Date().toString());
        billingHistory.setInvoice_id(session.getId());
        billingHistory.setStatus("Completed");
        billingHistoryService.save(billingHistory);

        // Update company status
        companyService.findAndUpdateCompanyLevel(companyId, "3");
        cmpPostedJobsService.findAndUpdateCompanyLevel(companyId, "3");
        credentialsService.findAndUpdateCompanyLevel(companyId, "3");

        prePaymentService.updateSubscriptionId(subscription.getId(), companyId);
        prePaymentService.updatePaymentMethodId(paymentMethod.getId(), companyId);
        prePaymentService.updateStatus(companyId, "Completed");
    }

    private void handleInvoiceCreated(Event event) throws StripeException {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer().getObject().get();
        String companyId = null;

        // Retrieve the associated customer
        Customer customer = Customer.retrieve(invoice.getCustomer());
        if (customer.getMetadata() != null && customer.getMetadata().containsKey("company_id")) {
            companyId = customer.getMetadata().get("company_id");
        }

        InvoicesModel invoiceModel = new InvoicesModel();
        invoiceModel.setInvoiceId(invoice.getId());
        invoiceModel.setCompanyId(companyId);
        invoiceModel.setSubscriptionId(invoice.getSubscription());
        invoiceModel.setAmountDue(String.valueOf(invoice.getAmountDue()));
        invoiceModel.setStatus(invoice.getStatus());
        invoiceModel.setBillingDate(new Date(invoice.getCreated() * 1000L));
        invoiceModel.setDueDate(new Date(invoice.getDueDate() * 1000L));
        invoiceModel.setPeriodStart(new Date(invoice.getPeriodStart() * 1000L));
        invoiceModel.setPeriodEnd(new Date(invoice.getPeriodEnd() * 1000L));

        invoiceRepository.save(invoiceModel);
        prePaymentService.updateInvoiceId(invoice.getId(), companyId);
    }

    private void handleInvoiceUpdated(Event event) throws StripeException {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer().getObject().get();

        String companyId = null;

        Customer customer = Customer.retrieve(invoice.getCustomer());
        if (customer.getMetadata() != null && customer.getMetadata().containsKey("company_id")) {
            companyId = customer.getMetadata().get("company_id");
        }

        InvoicesModel existingInvoice = invoiceRepository.findByInvoiceId(invoice.getId());
        if (existingInvoice != null) {
            existingInvoice.setAmountDue(String.valueOf(invoice.getAmountDue()));
            existingInvoice.setStatus(invoice.getStatus());
            existingInvoice.setBillingDate(new Date(invoice.getCreated() * 1000L));
            existingInvoice.setDueDate(new Date(invoice.getDueDate() * 1000L));
            existingInvoice.setPeriodStart(new Date(invoice.getPeriodStart() * 1000L));
            existingInvoice.setPeriodEnd(new Date(invoice.getPeriodEnd() * 1000L));
            invoiceRepository.save(existingInvoice);

            prePaymentService.updateInvoiceId(invoice.getId(), companyId);
        }
    }

    private void handleInvoicePaymentSucceeded(Event event) {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer().getObject().get();
        String subscriptionId = invoice.getSubscription();
        String amountPaid = String.valueOf(invoice.getAmountPaid());

        // Update subscription billing history
        subscriptionService.updateBillingHistory(subscriptionId, amountPaid, "Paid");
    }

    private void handleInvoicePaymentFailed(Event event) {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer().getObject().get();
        String subscriptionId = invoice.getSubscription();

        // Update subscription as inactive
        subscriptionService.markAsInactive(subscriptionId);
    }

    private void handleSubscriptionUpdated(Event event) {
        Subscription subscription = (Subscription) event.getDataObjectDeserializer().getObject().get();
        SubscriptionsModel subscriptionsModel = new SubscriptionsModel();
        subscriptionsModel.setPlan_name(subscription.getItems().getData().get(0).getPlan().getNickname());
        subscriptionsModel.setCost(String.valueOf(subscription.getItems().getData().get(0).getPlan().getAmount() / 100.0));
        subscriptionsModel.setBilling_cycle(subscription.getBillingCycleAnchor().toString());
        subscriptionsModel.setStart_date(subscription.getCurrentPeriodStart().toString());
        subscriptionsModel.setEnd_date(subscription.getCurrentPeriodEnd().toString());
        subscriptionsModel.set_active(subscription.getStatus().equals("active"));
        subscriptionService.updateSubscription(subscription.getCustomer(), subscriptionsModel);
    }

    private void handleSubscriptionDeleted(Event event) {
        Subscription subscription = (Subscription) event.getDataObjectDeserializer().getObject().get();
        subscriptionService.markAsInactive(subscription.getId());
    }
}

