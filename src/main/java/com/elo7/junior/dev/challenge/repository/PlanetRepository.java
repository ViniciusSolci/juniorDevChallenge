package com.elo7.junior.dev.challenge.repository;

import com.elo7.junior.dev.challenge.entity.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {

}
