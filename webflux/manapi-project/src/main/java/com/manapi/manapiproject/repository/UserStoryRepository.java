package com.manapi.manapiproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manapi.manapiproject.model.userstory.UserStory;

public interface UserStoryRepository extends JpaRepository<UserStory, Long> {
    
}
