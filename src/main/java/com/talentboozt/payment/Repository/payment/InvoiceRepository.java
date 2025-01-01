package com.talentboozt.payment.Repository.payment;

import com.talentboozt.payment.Model.payment.InvoicesModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InvoiceRepository extends MongoRepository<InvoicesModel, String> {
    List<InvoicesModel> findByCompanyId(String companyId);
    InvoicesModel findByInvoiceId(String invoiceId);
}
