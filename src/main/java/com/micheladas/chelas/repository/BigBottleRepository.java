package com.micheladas.chelas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.micheladas.chelas.entity.BigBottle;

@Repository
public interface BigBottleRepository extends JpaRepository<BigBottle, Long> {

}
