package com.jonrib.tasks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jonrib.tasks.model.Download;
import com.jonrib.tasks.repository.DownloadRepository;

@Service
public class DownloadServiceImpl implements DownloadService{
	@Autowired
	private DownloadRepository downloadRepository;

	@Override
	public Download save(Download entity) {
		return downloadRepository.save(entity);
	}

	@Override
	public List<Download> findAll() {
		return downloadRepository.findAll();
	}
	
	
}
