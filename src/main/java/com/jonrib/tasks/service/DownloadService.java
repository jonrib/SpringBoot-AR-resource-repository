package com.jonrib.tasks.service;

import java.util.List;

import com.jonrib.tasks.model.Download;

public interface DownloadService {
	List<Download> findAll();
	Download save(Download entity);
	
}
