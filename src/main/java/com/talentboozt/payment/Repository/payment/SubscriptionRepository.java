package com.talentboozt.payment.Repository.payment;

import com.talentboozt.payment.Model.payment.SubscriptionsModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubscriptionRepository extends MongoRepository<SubscriptionsModel, String> {
    SubscriptionsModel findByCompanyId(String companyId);
}
