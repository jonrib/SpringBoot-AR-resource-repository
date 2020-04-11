package com.jonrib.tasks.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jonrib.tasks.model.Comment;
import com.jonrib.tasks.model.Download;
import com.jonrib.tasks.model.History;
import com.jonrib.tasks.model.PreviewImage;
import com.jonrib.tasks.model.ResourceEntry;
import com.jonrib.tasks.model.ResourceFile;
import com.jonrib.tasks.model.Role;
import com.jonrib.tasks.model.User;
import com.jonrib.tasks.repository.DownloadRepository;
import com.jonrib.tasks.repository.HistoryRepository;
import com.jonrib.tasks.repository.PreviewImageRepository;
import com.jonrib.tasks.repository.ResourceEntryRepository;
import com.jonrib.tasks.repository.ResourceFileRepository;

@Service
public class ResourceEntryServiceImpl implements ResourceEntryService {
	@Autowired
	private ResourceEntryRepository resourceEntryRepository;
	@Autowired
	private StorageService storageService;
	@Autowired
	private ResourceFileRepository resourceFileRepository;
	@Autowired
	private DownloadRepository downloadRepository;
	@Autowired
	private PreviewImageRepository previewImageRepository;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private UserService userService;
	@Autowired
	private HistoryRepository historyRepository;
	@Autowired
	private CommentService commentService;

	
	
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
		return (entity.isPrivate() && entity.getReaders().contains(userService.findByUsername(securityService.findLoggedInUsername(token)))) || entity.getAuthor().contains(userService.findByUsername(securityService.findLoggedInUsername(token)));
	}
	@Override
	public boolean canEdit(ResourceEntry entity, String token) {
		User user = userService.findByUsername(securityService.findLoggedInUsername(token));
		Set<Role> userRoles = user != null ? user.getRoles() : new HashSet<Role>();
		boolean isAdmin = false;
		for (Role role : userRoles) {
			if (role.getName().equals("Admin")) {
				isAdmin = true;
				break;
			}
		}
		return isAdmin || entity.getAuthor().contains(userService.findByUsername(securityService.findLoggedInUsername(token))) || entity.getEditors().contains(userService.findByUsername(securityService.findLoggedInUsername(token)));
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
