package com.car.platform.repos;

import com.car.platform.models.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DealerRepo extends JpaRepository<Dealer, Long>, JpaSpecificationExecutor<Dealer> {
    
}
