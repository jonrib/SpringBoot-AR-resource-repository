package com.jonrib.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonrib.tasks.model.Download;
import com.jonrib.tasks.model.Role;

public interface DownloadRepository extends JpaRepository<Download, Long>{

}
