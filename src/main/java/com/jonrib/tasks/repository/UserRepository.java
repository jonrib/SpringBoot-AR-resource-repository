package com.jonrib.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonrib.tasks.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
