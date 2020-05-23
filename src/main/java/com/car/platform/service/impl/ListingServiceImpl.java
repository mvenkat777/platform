/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.car.platform.service.impl;

import com.car.platform.dto.ListingSearch;
import com.car.platform.dto.SearchCriteria;
import com.car.platform.models.Listing;
import com.car.platform.models.Provider;
import com.car.platform.repos.ListingRepo;
import com.car.platform.service.ListingService;
import com.car.platform.service.QuerySpecification;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ListingServiceImpl implements ListingService {

    private final ListingRepo listingRepo;
    private final  QuerySpecification querySpecification;
    
    public ListingServiceImpl(ListingRepo listingRepo, QuerySpecification querySpecification) {
        this.listingRepo = listingRepo;
        this.querySpecification = querySpecification;
    }
    
    /** {@inheritDoc} */
    @Transactional
    @Override
    public void saveListing(Provider provider, List<Listing> listings) {
        try {
            ArrayList<Listing> listingsToSave = new ArrayList<>();
            listings.parallelStream().forEach((l) -> {
                Listing listing = getListingByCodeAndProvider(l.getCode(), provider);
                
                if (null == listing)
                    listingsToSave.add(l);
                else 
                    listingsToSave.add(updateListing(listing, l));
                
            });
            saveAll(listingsToSave); // save all at once
        } catch (Exception e) {
            System.out.println("An exception occurred while trying to save listing");
            //log.error("An exception occurred while trying to save listing", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Listing getListingByCodeAndProvider(String code, Provider provider) {
        Specification<Listing> codeSpec = (Specification<Listing>) querySpecification.buildQuery(new SearchCriteria("code", ":", code));
        Specification<Listing> providerSpec = (Specification<Listing>) querySpecification.buildQuery(new SearchCriteria("provider", ":", provider));
        return listingRepo.findOne(Specification.where(codeSpec).and(providerSpec)).orElseGet(() -> null);
    }
    
    /**
     * Updates Listing
     * @param listing
     * @param requestListing
     * @return 
     */
    private Listing updateListing(Listing listing, Listing requestListing) {
        listing.setMake(requestListing.getMake());
        listing.setColor(requestListing.getColor());
        listing.setModel(requestListing.getModel());
        listing.setKw(requestListing.getKw());
        listing.setPrice(requestListing.getPrice());
        listing.setYear(requestListing.getYear());
        return listing;
    }

    /** {@inheritDoc} */
    @Override
    public List<Listing> getAllListings() {
        return listingRepo.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public void saveAll(List<Listing> listings) {
        listingRepo.saveAll(listings);
    }

    /** {@inheritDoc} */
    @Override
    public List<ListingSearch> searchByQueryParameter(String searchParameter) {
        return listingRepo.getListingByParameter(searchParameter);
    }
    
}
