package com.jonrib.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonrib.tasks.model.ResourceEntry;

public interface ResourceEntryRepository extends JpaRepository<ResourceEntry, Long>{

}
