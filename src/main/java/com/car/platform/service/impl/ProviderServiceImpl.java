
package com.car.platform.service.impl;

import com.car.platform.dto.SearchCriteria;
import com.car.platform.models.Dealer;
import com.car.platform.models.Provider;
import com.car.platform.repos.ProviderRepo;
import com.car.platform.service.ProviderService;
import com.car.platform.service.QuerySpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ProviderServiceImpl implements ProviderService {
    
    private final ProviderRepo providerRepo;
    private final QuerySpecification querySpecification;
    
    @Autowired
    public ProviderServiceImpl(QuerySpecification querySpecification, ProviderRepo providerRepo) {
        this.querySpecification = querySpecification;
        this.providerRepo = providerRepo;
    }

    /** {@inheritDoc} */
    @Override
    public Provider getByProviderNameAndDealer(Dealer dealer, String providerName) {
        Specification<Provider> dealerSpec = (Specification<Provider>) querySpecification.buildQuery(new SearchCriteria("dealer", ":", dealer));
        Specification<Provider> providerSpec = (Specification<Provider>) querySpecification.buildQuery(new SearchCriteria("name", ":", providerName));
        return providerRepo.findOne(Specification.where(dealerSpec).and(providerSpec)).orElseGet(() -> null);
    }
    
}
