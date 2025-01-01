package com.talentboozt.payment.Repository.payment;

import com.talentboozt.payment.Model.payment.PaymentMethodsModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaymentMethodRepository extends MongoRepository<PaymentMethodsModel, String> {
    List<PaymentMethodsModel> findByCompanyId(String companyId);
}
