package com.jonrib.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonrib.tasks.model.Download;

public interface DownloadRepository extends JpaRepository<Download, Long>{

}
