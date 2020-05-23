
package com.car.platform.service;

import com.car.platform.dto.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;


public interface QuerySpecification {
    /**
     * Builds a query specification object based on a user entity
     * @param criteria
     * @return 
     */
    Specification<? extends Object> buildQuery(SearchCriteria criteria);
}
