package com.manapi.manapiproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manapi.manapiproject.model.effort.Effort;

public interface EffortRepository extends JpaRepository<Effort, Long> {
    
}
