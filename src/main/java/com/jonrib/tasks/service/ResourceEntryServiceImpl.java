package com.jonrib.tasks.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jonrib.tasks.model.Download;
import com.jonrib.tasks.model.PreviewImage;
import com.jonrib.tasks.model.ResourceEntry;
import com.jonrib.tasks.model.ResourceFile;
import com.jonrib.tasks.repository.DownloadRepository;
import com.jonrib.tasks.repository.PreviewImageRepository;
import com.jonrib.tasks.repository.ResourceEntryRepository;
import com.jonrib.tasks.repository.ResourceFileRepository;

@Service
public class ResourceEntryServiceImpl implements ResourceEntryService {
	@Autowired
	private ResourceEntryRepository resourceEntryRepository;
	@Autowired
	private StorageService storageService;
	@Autowired
	private ResourceFileRepository resourceFileRepository;
	@Autowired
	private DownloadRepository downloadRepository;
	@Autowired
	private PreviewImageRepository previewImageRepository;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private UserService userService;
	
	
	
	@Override
	public List<ResourceEntry> findAll() {
		return resourceEntryRepository.findAll();
	}
	@Override
	public Optional<ResourceEntry> findById(Long id) {
		return resourceEntryRepository.findById(id);
	}
	@Override
	public ResourceEntry save(ResourceEntry entity) {
		return resourceEntryRepository.save(entity);
	}
	@Override
	public void delete(ResourceEntry entity) {
		for (ResourceFile resFile : entity.getFiles()) {
			storageService.delete(resFile.getFilePath());
			resourceFileRepository.delete(resFile);
		}
		for (Download download : entity.getDownloads()) {
			downloadRepository.delete(download);
		}
		for (PreviewImage resFile : entity.getImages()) {
			storageService.delete(resFile.getFilePath());
			previewImageRepository.delete(resFile);
		}
		resourceEntryRepository.delete(entity);
	}
	@Override
	public boolean canRead(ResourceEntry entity) {
		return (entity.isPrivate() && entity.getReaders().contains(userService.findByUsername(securityService.findLoggedInUsername()))) || entity.getAuthor().contains(userService.findByUsername(securityService.findLoggedInUsername()));
	}
	@Override
	public boolean canEdit(ResourceEntry entity) {
		return entity.getAuthor().contains(userService.findByUsername(securityService.findLoggedInUsername())) || entity.getEditors().contains(userService.findByUsername(securityService.findLoggedInUsername()));
	}
	

}
