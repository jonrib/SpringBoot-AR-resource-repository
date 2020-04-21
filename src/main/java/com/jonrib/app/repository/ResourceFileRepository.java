package com.jonrib.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonrib.app.model.ResourceFile;

public interface ResourceFileRepository extends JpaRepository<ResourceFile, Long>{

}
