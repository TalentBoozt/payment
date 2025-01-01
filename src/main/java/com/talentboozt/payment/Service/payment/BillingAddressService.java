package com.talentboozt.payment.Service.payment;

import com.talentboozt.payment.Model.payment.BillingAddressModel;
import com.talentboozt.payment.Repository.payment.BillingAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingAddressService {

    @Autowired
    BillingAddressRepository billingAddressRepository;

    public BillingAddressModel updateBillingAddress(String companyId, BillingAddressModel address) {
        List<BillingAddressModel> addresses = billingAddressRepository.findByCompanyId(companyId);
        if (addresses.size() > 0) {
            addresses.get(0).setStreet(address.getStreet());
            addresses.get(0).setCity(address.getCity());
            addresses.get(0).setState(address.getState());
            addresses.get(0).setPostal_code(address.getPostal_code());
            addresses.get(0).setCountry(address.getCountry());
            return billingAddressRepository.save(addresses.get(0));
        }
        return billingAddressRepository.save(address);
    }
}
