package com.jonrib.tasks.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
	public Resource loadAsResource(byte[] bytes, String extension) {
		try {
			File actualFile = new File(extension);
			OutputStream os = new FileOutputStream(actualFile);
		    os.write(bytes);
			Resource resource = new FileSystemResource(actualFile);
			os.close();
			return resource;
		}catch (Exception e) {
			return null;
		}
	}

}
