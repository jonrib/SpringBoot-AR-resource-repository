package com.jonrib.app.service;

import java.util.List;

import com.jonrib.app.model.Role;

public interface RoleService {
	Role findById(Long id);
	List<Role> findAll();
	void save(Role role);
	void delete(Role role);
	Role findByName(String name);
}
