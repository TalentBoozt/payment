package com.talentboozt.payment.Controller.payment;

import com.talentboozt.payment.Model.payment.PaymentMethodsModel;
import com.talentboozt.payment.Service.payment.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/payment-methods")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("/get/{companyId}")
    public ResponseEntity<List<PaymentMethodsModel>> getPaymentMethods(@PathVariable String companyId) {
        List<PaymentMethodsModel> paymentMethods = paymentMethodService.getPaymentMethods(companyId);
        return ResponseEntity.ok(paymentMethods);
    }

    @PostMapping("/update/{companyId}")
    public ResponseEntity<PaymentMethodsModel> addPaymentMethod(
            @PathVariable String companyId,
            @RequestBody PaymentMethodsModel paymentMethod) {
        PaymentMethodsModel addedPaymentMethod = paymentMethodService.addPaymentMethod(companyId, paymentMethod);
        return ResponseEntity.ok(addedPaymentMethod);
    }
}
