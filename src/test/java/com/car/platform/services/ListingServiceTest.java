
package com.car.platform.services;

import com.car.platform.dto.ListingSearch;
import com.car.platform.models.Dealer;
import com.car.platform.models.Listing;
import com.car.platform.models.Provider;
import com.car.platform.repos.ListingRepo;
import com.car.platform.service.ListingService;
import com.car.platform.service.QuerySpecification;
import com.car.platform.service.impl.ListingServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


public class ListingServiceTest {
    
    private ListingRepo listingRepo;
    private QuerySpecification querySpecification;
    private ListingService listingService;
    
    @Before
    public void setup() {
        listingRepo = mock(ListingRepo.class);
        querySpecification = mock(QuerySpecification.class);
        listingService = new ListingServiceImpl(listingRepo, querySpecification);
    }
    
    /**
     * Test case get listing by code and provider
     * @throws Exception 
     */
    @Test
    public void getListingByCodeAndProvider_whenCodeAndProvider_thenReturnListing() throws Exception {
        when((Specification<Listing>) querySpecification.buildQuery(any())).thenReturn(stubListingSpecification());
        when(listingRepo.findOne(isA(Specification.class))).thenReturn(Optional.of(mockListing()));
        
        // make the service call
        Listing listing = listingService.getListingByCodeAndProvider("aa", new Provider());
        
        assertNotNull(listing);
        assertEquals(listing.getCode(), mockListing().getCode());
        assertEquals(listing.getMake(), mockListing().getMake());
        assertThat(listing.getYear(), is(2015));
    }
    
    /**
     * Test case for get all listings
     * @throws Exception 
     */
    @Test
    public void getAllListings_thenReturnListOfListings() throws Exception {
        when(listingRepo.findAll()).thenReturn(mockListings());
        
        // make the service call
        List<Listing> listings = listingService.getAllListings();
        
        assertNotNull(listings);
        assertFalse(listings.isEmpty());
        assertThat(listings, containsInAnyOrder( // confirm that each of the elements returned the appropriate expected attributes
                hasProperty("code", is("a"))
                ));
        assertThat(listings.size(), is(mockListings().size())); // confirm that the sizes are the same
    }
    
    /**
     * Test case for searching listing
     * @throws Exception 
     */
    @Test
    public void searchByQueryParameter_whenSearchParameter_thenReturnListingSearch() throws Exception {
        when(listingRepo.getListingByParameter(any())).thenReturn(mockListingSearch());
        
        // make service call
        List<ListingSearch> listings = listingService.searchByQueryParameter("benz");
        
        assertNotNull(listings);
        assertFalse(listings.isEmpty());
        assertThat(listings, containsInAnyOrder( // confirm that each of the elements returned the appropriate expected attributes
                hasProperty("code", is("bb"))
                ));
        assertThat(listings.size(), is(mockListingSearch().size())); // confirm that the sizes are the same
    }

    /**
     * Mock Listings
     * @return
     */
    public static List<Listing> mockListings() {
        List<Listing> listings = new ArrayList<>();
        Listing listing1 = new Listing();
        listing1.setCode("a");
        listing1.setColor("red");
        listing1.setKw(123);
        listing1.setMake("mercedes");
        listing1.setModel("e300");
        listing1.setPrice(new BigDecimal(1000));
        listing1.setYear(2015);
        listings.add(listing1);
        return listings;
    }

    /**
     * Mock Listing Search
     * @return
     */
    public static List<ListingSearch> mockListingSearch() {
        List<ListingSearch> listings = new ArrayList<>();
        ListingSearch listing = new ListingSearch("bb", "volks", "passat", 28, 2016, "red", new BigDecimal(5000));
        listings.add(listing);
        return listings;
    }

    public static Specification<Provider> stubProviderSpecification() {
        return (Root<Provider> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            return cb.equal(root.get("key"), "value");
        };
    }

    public static Listing mockListing() {
        Listing listing = new Listing();
        listing.setCode("a");
        listing.setColor("red");
        listing.setKw(123);
        listing.setMake("mercedes");
        listing.setModel("e300");
        listing.setPrice(new BigDecimal(1000));
        listing.setYear(2015);
        return listing;
    }

    public static Provider stubProvider() {
        return new Provider(1L, new Dealer(1L, "Benz", new Date(), new Date()), "Benz Provider", new Date(), new Date());
    }

    public static Specification<Listing> stubListingSpecification() {
        return (Root<Listing> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            return cb.equal(root.get("key"), "value");
        };
    }
}
