
package com.car.platform.service;

import com.car.platform.dto.ListingSearch;
import com.car.platform.models.Listing;
import com.car.platform.models.Provider;
import java.util.List;


public interface ListingService {
    /**
     * Save Listings by provider
     * @param provider
     * @param listings 
     */
    void saveListing(Provider provider, List<Listing> listings);
    /**
     * Save all listings
     * @param listings 
     */
    void saveAll(List<Listing> listings);
    
    /**
     * Get Listing by code and provider
     * @param code
     * @param provider
     * @return 
     */
    Listing getListingByCodeAndProvider(String code, Provider provider);
    /**
     * Gets all listings
     * @return 
     */
    List<Listing> getAllListings();
    
    /**
     * Search by a selected query parameter
     * @param searchParameter
     * @return 
     */
    List<ListingSearch> searchByQueryParameter(String searchParameter);
}
