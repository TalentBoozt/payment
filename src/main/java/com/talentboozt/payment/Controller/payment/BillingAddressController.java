package com.talentboozt.payment.Controller.payment;

import com.talentboozt.payment.Model.payment.BillingAddressModel;
import com.talentboozt.payment.Service.payment.BillingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/billing-address")
public class BillingAddressController {

    @Autowired
    BillingAddressService billingAddressService;

    @PutMapping("/update/{companyId}")
    public BillingAddressModel updateBillingAddress(@PathVariable String companyId, @RequestBody BillingAddressModel billingAddress) {
        return billingAddressService.updateBillingAddress(companyId, billingAddress);
    }
}
