package tests;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.jonrib.tasks.WebApplication;
import com.jonrib.tasks.service.StorageService;
import com.jonrib.tasks.service.StorageServiceImpl;

public class StorageServiceTests {
	private StorageService storageService = new StorageServiceImpl();
	
	@Test
	void store() throws IllegalStateException, IOException {
		MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
		storageService.store(firstFile, "testPath1");
		File file = new File("testPath1/filename.txt");
		file.delete();
		file = new File("testPath1");
		file.delete();
	}
	
	@Test
	void delete() throws IOException {
		File file = new File("testPath.txt");
		file.createNewFile();
		storageService.delete("testPath.txt");
	}
	
	@Test
	void deleteAll() throws IOException {
		File file = new File("newDIR");
		file.mkdir();
		file = new File("newDIR\\test.txt");
		file.createNewFile();
		storageService.deleteAll("newDIR");
	}
	
	@Test
	void loadAsResource() throws IOException {
		File file = new File("testPath.txt");
		file.createNewFile();
		storageService.loadAsResource("testPath.txt");
		file.delete();
	}
}
