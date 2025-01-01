package com.talentboozt.payment.Controller.payment;

import com.talentboozt.payment.Model.payment.PrePaymentModel;
import com.talentboozt.payment.Service.payment.PrePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/pre-payment")
public class PrePaymentController {

    @Autowired
    private PrePaymentService prePaymentService;

    @PostMapping("/save")
    public PrePaymentModel addPrePayment(@RequestBody PrePaymentModel prePaymentModel){
        return prePaymentService.save(prePaymentModel);
    }
}
