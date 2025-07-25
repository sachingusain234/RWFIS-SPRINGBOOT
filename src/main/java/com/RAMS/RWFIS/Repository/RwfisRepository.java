package com.RAMS.RWFIS.Repository;

import com.RAMS.RWFIS.Entity.RwfisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RwfisRepository extends JpaRepository<RwfisEntity, Long> {
    Optional<RwfisEntity> findByRsiId(Long rsiId);

}
