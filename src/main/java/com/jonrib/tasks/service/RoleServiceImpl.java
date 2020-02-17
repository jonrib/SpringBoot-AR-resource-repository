package com.jonrib.tasks.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jonrib.tasks.model.Role;
import com.jonrib.tasks.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService{
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public Role findById(Long id) {
		Optional<Role> role = roleRepository.findById(id);
		if (role.isPresent())
			return role.get();
		return null;
	}

	@Override
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	@Override
	public void save(Role role) {
		roleRepository.save(role);
	}

	@Override
	public void delete(Role role) {
		roleRepository.delete(role);
	}

	@Override
	public Role findByName(String name) {
		for (Role role : roleRepository.findAll()) {
			if (role.getName().equals(name)) {
				return role;
			}
		}
		return null;
	}

}
