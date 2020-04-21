package com.jonrib.tasks.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jonrib.tasks.model.ResourceEntry;
import com.jonrib.tasks.model.Role;
import com.jonrib.tasks.model.User;
import com.jonrib.tasks.repository.ResourceEntryRepository;

@Service
public class ResourceEntryServiceImpl implements ResourceEntryService {
	@Autowired
	private ResourceEntryRepository resourceEntryRepository;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private UserService userService;

	public ResourceEntryServiceImpl() { }
	
	public ResourceEntryServiceImpl(ResourceEntryRepository resourceEntryRepository2, SecurityService securityService2,
			UserService userService2) {
		this.resourceEntryRepository = resourceEntryRepository2;
		this.securityService = securityService2;
		this.userService = userService2;
	}
	
	@Override
	public List<ResourceEntry> findAll() {
		return resourceEntryRepository.findAll();
	}
	@Override
	public Optional<ResourceEntry> findById(Long id) {
		return resourceEntryRepository.findById(id);
	}
	@Override
	public ResourceEntry save(ResourceEntry entity) {
		return resourceEntryRepository.save(entity);
	}
	@Override
	public void delete(ResourceEntry entity) {
		entity.setAuthor(null);
		entity.setEditors(null);
		entity.setReaders(null);
		resourceEntryRepository.delete(entity);
	}
	@Override
	public boolean canRead(ResourceEntry entity, String token) {
		if (!entity.isPrivate())
			return true;
		if (token.equals(""))
			return false;
		String actualUsername = securityService.findLoggedInUsername(token);		
		return (entity.isPrivate() && entity.getReaders().stream().anyMatch(x -> x.getUsername().equals(actualUsername))) || entity.getAuthor().stream().anyMatch(x -> x.getUsername().equals(actualUsername));
	}
	@Override
	public boolean canEdit(ResourceEntry entity, String token) {
		if (token.equals(""))
			return false;
		String username = securityService.findLoggedInUsername(token);
		User user = userService.findByUsername(username);
		Set<Role> userRoles = user != null ? user.getRoles() : new HashSet<Role>();
		boolean isAdmin = false;
		for (Role role : userRoles) {
			if (role.getName().equals("Admin")) {
				isAdmin = true;
				break;
			}
		}
		return isAdmin || entity.getAuthor().stream().anyMatch(x -> x.getUsername().equals(username)) || entity.getEditors().stream().anyMatch(x -> x.getUsername().equals(username));
	}
	@Override
	public List<ResourceEntry> findByCategory(String category) {
		return resourceEntryRepository.findByCategory(category);
	}
	@Override
	public List<ResourceEntry> findByTagsIn(List<String> tags) {
		HashSet<ResourceEntry> entries = new HashSet<ResourceEntry>();
		entries.addAll(resourceEntryRepository.findByTagsIn(tags));
		return new ArrayList<ResourceEntry>(entries);
	}
	

}
