package com.manapi.manapiproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manapi.manapiproject.model.milestone.Milestone;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {

}
