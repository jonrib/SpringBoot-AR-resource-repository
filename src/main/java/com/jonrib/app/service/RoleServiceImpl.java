package com.jonrib.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jonrib.app.model.Role;
import com.jonrib.app.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService{
	@Autowired
	private RoleRepository roleRepository;
	
	public RoleServiceImpl() { }
	
	public RoleServiceImpl(RoleRepository roleRepository2) {
		this.roleRepository = roleRepository2;
	}

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
		List<Role> roles = roleRepository.findByName(name);
		return roles.size() > 0 ? roles.get(0) : null; 
	}

}
