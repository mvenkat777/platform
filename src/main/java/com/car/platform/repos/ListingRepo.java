package com.car.platform.repos;

import com.car.platform.dto.ListingSearch;
import com.car.platform.models.Listing;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ListingRepo extends JpaRepository<Listing, Long>, JpaSpecificationExecutor<Listing> {
    /**
     * Gets query details by a search parameter
     * @param searchParameter
     * @return 
     */
    @Query("SELECT new com.car.platform.dto.ListingSearch(l.code, l.make, l.model, l.kw, l.year, l.color, l.price)"
            + " FROM Listing l WHERE l.make LIKE %?1% OR l.model LIKE %?1% OR CAST(l.year AS string) LIKE %?1% OR l.color LIKE %?1%")
    List<ListingSearch> getListingByParameter(String searchParameter);
}
