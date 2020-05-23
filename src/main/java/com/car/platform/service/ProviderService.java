
package com.car.platform.service;

import com.car.platform.models.Dealer;
import com.car.platform.models.Provider;


public interface ProviderService {
    /**
     * Get provider details by name and dealer
     * @param dealer
     * @param providerName
     * @return 
     */
    Provider getByProviderNameAndDealer(Dealer dealer, String providerName);
}
