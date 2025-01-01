package com.talentboozt.payment.Controller.mail;

import com.talentboozt.payment.DTO.ApiResponse;
import com.talentboozt.payment.DTO.mail.BankPaymentDTO;
import com.talentboozt.payment.Service.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/bank-payment")
    public ResponseEntity<ApiResponse> bankPayment(@RequestBody BankPaymentDTO bankPaymentDTO) {
        emailService.bankPayment(bankPaymentDTO);
        return ResponseEntity.ok(new ApiResponse("Email sent successfully"));
    }
}
