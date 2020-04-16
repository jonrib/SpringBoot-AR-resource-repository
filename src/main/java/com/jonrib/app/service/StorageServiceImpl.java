package com.jonrib.app.service;

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
	public Resource loadAsResource(byte[] bytes) {
		try {
			File actualFile = new File("temp");
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
