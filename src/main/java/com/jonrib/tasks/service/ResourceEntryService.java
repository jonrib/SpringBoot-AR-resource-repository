package com.jonrib.tasks.service;

import java.util.List;
import java.util.Optional;

import com.jonrib.tasks.model.ResourceEntry;

public interface ResourceEntryService {
	List<ResourceEntry> findAll();
	Optional<ResourceEntry> findById(Long id);
	ResourceEntry save(ResourceEntry entity);
	void delete(ResourceEntry entity);
	boolean canRead(ResourceEntry entity);
	boolean canEdit(ResourceEntry entity);
}
