package com.jonrib.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonrib.tasks.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
}
