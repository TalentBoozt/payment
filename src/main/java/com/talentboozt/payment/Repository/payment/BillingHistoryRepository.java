package com.talentboozt.payment.Repository.payment;

import com.talentboozt.payment.Model.payment.BillingHistoryModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BillingHistoryRepository extends MongoRepository<BillingHistoryModel, String> {
    List<BillingHistoryModel> findByCompanyId(String companyId);
}

