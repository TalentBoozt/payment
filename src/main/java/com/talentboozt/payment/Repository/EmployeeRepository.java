package com.talentboozt.payment.Repository;

import com.talentboozt.payment.Model.EmployeeModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends MongoRepository<EmployeeModel, String> {
    List<EmployeeModel> findAllBy(Pageable pageable);
}