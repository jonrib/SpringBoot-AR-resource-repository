package com.jonrib.tasks.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void init();

	void store(MultipartFile file, String storagePath) throws IllegalStateException, IOException;

	Stream<Path> loadAll();

	Path load(String filename);

	void deleteAll(String dir);
	
	void delete(String path);

	Resource loadAsResource(String path);
	

}
