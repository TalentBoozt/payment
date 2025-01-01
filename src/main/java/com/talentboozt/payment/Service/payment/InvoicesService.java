package com.talentboozt.payment.Service.payment;

import com.talentboozt.payment.Model.payment.InvoicesModel;
import com.talentboozt.payment.Repository.payment.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoicesService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public List<InvoicesModel> getAllInvoices() { return invoiceRepository.findAll(); }

    public List<InvoicesModel> getInvoicesByCompanyId(String companyId) { return invoiceRepository.findByCompanyId(companyId); }
}
