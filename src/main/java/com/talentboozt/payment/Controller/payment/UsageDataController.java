package com.talentboozt.payment.Controller.payment;

import com.talentboozt.payment.Model.payment.UsageDataModel;
import com.talentboozt.payment.Service.payment.UsageDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/usage")
public class UsageDataController {

    @Autowired
    private UsageDataService usageDataService;

    @GetMapping("/get/{companyId}")
    public ResponseEntity<UsageDataModel> getUsageData(@PathVariable String companyId) {
        UsageDataModel usageData = usageDataService.getUsageData(companyId);
        if (usageData != null) {
            return ResponseEntity.ok(usageData);
        }
        return ResponseEntity.notFound().build();
    }
}