package com.manapi.manapigateway.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.manapi.manapigateway.model.project.Project;

public interface ProjectRepository extends MongoRepository<Project, String> {
    
}
