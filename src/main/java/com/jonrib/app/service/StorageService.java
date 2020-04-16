package com.jonrib.app.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
	Resource loadAsResource(byte[] bytes);
}
