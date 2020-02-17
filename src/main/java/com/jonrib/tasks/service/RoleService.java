package com.jonrib.tasks.service;

import java.util.List;

import com.jonrib.tasks.model.Role;

public interface RoleService {
	Role findById(Long id);
	List<Role> findAll();
	void save(Role role);
	void delete(Role role);
	Role findByName(String name);
}
