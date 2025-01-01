package com.talentboozt.payment.Service.payment;

import com.talentboozt.payment.Model.payment.UsageDataModel;
import com.talentboozt.payment.Repository.payment.UsageDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsageDataService {

    @Autowired
    private UsageDataRepository usageDataRepository;

    public UsageDataModel getUsageData(String companyId) {
        return usageDataRepository.findByCompanyId(companyId);
    }
}

