package com.jonrib.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonrib.tasks.model.Role;
import com.jonrib.tasks.model.UserGroup;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long>{

}
