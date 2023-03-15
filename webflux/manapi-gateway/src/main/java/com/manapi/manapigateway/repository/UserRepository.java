package com.manapi.manapigateway.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.manapi.manapigateway.model.user.User;

public interface UserRepository extends MongoRepository<User, String> {
    
}
