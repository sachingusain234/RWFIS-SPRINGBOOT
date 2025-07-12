package com.RAMS.RWFIS.Repository;

import com.RAMS.RWFIS.Entity.RwfisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RwfisRepository extends JpaRepository<RwfisEntity, Long> {
    // You can define custom queries here if needed
}
