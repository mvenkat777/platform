package com.car.platform.repos;

import com.car.platform.models.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProviderRepo extends JpaRepository<Provider, Long>, JpaSpecificationExecutor<Provider> {
    
}
