package com.elo7.junior.dev.challenge.repository;

import com.elo7.junior.dev.challenge.entity.Rocket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RocketRepository extends JpaRepository<Rocket, Long> {
    List<Rocket> findByAllocatedPlanetId(long planetId);
}