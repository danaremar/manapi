package com.manapi.manapiproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manapi.manapiproject.model.sprint.Sprint;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
    
}
