
package com.car.platform.service.impl;

import com.car.platform.models.Dealer;
import com.car.platform.repos.DealerRepo;
import com.car.platform.service.DealerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class DealerServiceImpl implements DealerService {
    
    private final DealerRepo dealerRepo;
    
    @Autowired
    public DealerServiceImpl(DealerRepo dealerRepo) {
        this.dealerRepo = dealerRepo;
    }

    /** {@inheritDoc} */
    @Override
    public Dealer findDealerById(Long id) {
        return dealerRepo.findById(id).orElseGet(() -> null);
    }
}
