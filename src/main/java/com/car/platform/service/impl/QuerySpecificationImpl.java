/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.car.platform.service.impl;

import com.car.platform.dto.ListingSearch;
import com.car.platform.dto.SearchCriteria;
import com.car.platform.dto.SearchCriteriaOperation;
import com.car.platform.models.Listing;
import com.car.platform.service.QuerySpecification;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class QuerySpecificationImpl implements QuerySpecification {

    /** {@inheritDoc} */
    @Override
    public Specification<? extends Object> buildQuery(SearchCriteria criteria) {
        return (Root<Object> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            // handles equals operation
            if (criteria.getOperation().equals(SearchCriteriaOperation.EQUAL_OPERATION))
                return cb.equal(root.get(criteria.getKey()), criteria.getValue());
            
            return null;
        };
    }
    
}
