package com.manapi.manapigateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.manapi.manapigateway.model.users.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
