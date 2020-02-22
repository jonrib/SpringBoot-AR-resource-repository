package com.jonrib.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonrib.tasks.model.ResourceFile;

public interface ResourceFileRepository extends JpaRepository<ResourceFile, Long>{

}
