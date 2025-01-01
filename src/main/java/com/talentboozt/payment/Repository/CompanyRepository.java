package com.talentboozt.payment.Repository;

import com.talentboozt.payment.Model.CompanyModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends MongoRepository<CompanyModel, String> {
    Optional<List<CompanyModel>> findAllByCompanyType(String type);
}
