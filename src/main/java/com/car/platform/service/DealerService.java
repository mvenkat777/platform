package com.car.platform.service;

import com.car.platform.models.Dealer;

public interface DealerService {
    /**
     * Get dealer by ID
     * @param id
     * @return 
     */
    Dealer findDealerById(Long id);
}
