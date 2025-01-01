package com.talentboozt.payment.Repository.payment;

import com.talentboozt.payment.Model.payment.BillingAddressModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BillingAddressRepository extends MongoRepository<BillingAddressModel, String> {
    List<BillingAddressModel> findByCompanyId(String companyId);
}
