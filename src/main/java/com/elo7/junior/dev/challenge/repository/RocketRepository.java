package com.elo7.junior.dev.challenge.repository;

import com.elo7.junior.dev.challenge.entity.Rocket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RocketRepository extends JpaRepository<Rocket, Long> {
    @Query("SELECT * FROM rocket r where r.allocated_planet_id = :planetId")
    List<Rocket> findByPlanetId(@Param("planet") long planetId);
}