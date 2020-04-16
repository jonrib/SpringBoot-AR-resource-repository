package com.jonrib.tasks.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void init();

	Resource loadAsResource(byte[] bytes);
	

}
