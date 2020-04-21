package com.jonrib.app.service;

import java.util.List;
import java.util.Optional;

import com.jonrib.app.model.ResourceEntry;

public interface ResourceEntryService {
	List<ResourceEntry> findAll();
	Optional<ResourceEntry> findById(Long id);
	ResourceEntry save(ResourceEntry entity);
	void delete(ResourceEntry entity);
	boolean canRead(ResourceEntry entity, String token);
	boolean canEdit(ResourceEntry entity, String token);
	List<ResourceEntry> findByCategory(String category);
	List<ResourceEntry> findByTagsIn(List<String> tags);
}
