package com.manapi.manapigateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manapi.manapigateway.model.users.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
