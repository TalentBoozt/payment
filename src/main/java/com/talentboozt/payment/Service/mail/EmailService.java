package com.talentboozt.payment.Service.mail;

import com.talentboozt.payment.DTO.mail.BankPaymentDTO;
import com.talentboozt.payment.Utils.ConfigUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    ConfigUtility configUtil;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public void bankPayment(BankPaymentDTO bankPaymentDTO) {
        String to = configUtil.getProperty("CONTACT_ME_EMAIL");
        String mailSubject = "Bank Payment Request - " + bankPaymentDTO.getName();
        String message = bankPaymentDTO.getCompanyId()+" Requested Bank Payment! \n\nSlip Url: " + bankPaymentDTO.getSlipUrl();
        String body = "Name: " + bankPaymentDTO.getName() + "\nCountry: " + bankPaymentDTO.getCountry() + "\nPhone: " + bankPaymentDTO.getPhone() + "\n\n" + message;

        sendSimpleEmail(to, mailSubject, body);
    }
}
