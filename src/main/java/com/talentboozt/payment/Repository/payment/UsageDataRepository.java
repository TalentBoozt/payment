package com.talentboozt.payment.Repository.payment;

import com.talentboozt.payment.Model.payment.UsageDataModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsageDataRepository extends MongoRepository<UsageDataModel, String> {
    UsageDataModel findByCompanyId(String companyId);
}
