package com.elo7.junior.dev.challenge.repository;

import com.elo7.junior.dev.challenge.entity.Rocket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RocketRepository extends JpaRepository<Rocket, Long> {
    Slice<Rocket> findByAllocatedPlanetId(Pageable pageable, long allocatedPlanetId);

    boolean existsByAllocatedPlanetId(long allocatedPlanetId);
}