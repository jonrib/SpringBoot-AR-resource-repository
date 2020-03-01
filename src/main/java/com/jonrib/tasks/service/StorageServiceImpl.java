package com.jonrib.tasks.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class StorageServiceImpl implements StorageService {
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void store(MultipartFile file, String path) throws IllegalStateException, IOException {
		File directory = new File(path);
	    if (! directory.exists()){
	        directory.mkdirs();
	    }
		file.transferTo(new File(path+"/"+file.getOriginalFilename()));
	}

	@Override
	public Stream<Path> loadAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path load(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource loadAsResource(String path) {
		File actualFile = new File(path);
		Resource resource = new FileSystemResource(actualFile);
		return resource;
	}
	private boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	@Override
	public void deleteAll(String dir) {
		File directory = new File(dir);
		deleteDirectory(directory);
	}

	@Override
	public void delete(String path) {
		File actualFile = new File(path);
		actualFile.delete();
	}

}
