package com.talentboozt.payment.Repository;

import com.talentboozt.payment.Model.CmpPostedJobsModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CmpPostedJobsRepository extends MongoRepository<CmpPostedJobsModel, String> {

    List<CmpPostedJobsModel> findByCompanyId(String companyId);
}
