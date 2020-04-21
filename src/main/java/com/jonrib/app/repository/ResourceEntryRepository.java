package com.jonrib.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonrib.app.model.ResourceEntry;

public interface ResourceEntryRepository extends JpaRepository<ResourceEntry, Long>{
	List<ResourceEntry> findByCategory(String category);
	List<ResourceEntry> findByTagsIn(List<String> tags);
}
