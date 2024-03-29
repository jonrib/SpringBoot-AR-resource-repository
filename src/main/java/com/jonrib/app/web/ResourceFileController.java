package com.jonrib.app.web;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonrib.app.exceptions.BadResourceFileForEntryException;
import com.jonrib.app.exceptions.ResourceEntryNoAccessException;
import com.jonrib.app.exceptions.ResourceEntryNotFoundException;
import com.jonrib.app.model.History;
import com.jonrib.app.model.ResourceEntry;
import com.jonrib.app.model.ResourceFile;
import com.jonrib.app.repository.HistoryRepository;
import com.jonrib.app.repository.ResourceFileRepository;
import com.jonrib.app.service.ResourceEntryService;
import com.jonrib.app.service.SecurityService;
import com.jonrib.app.service.StorageService;

@Controller
public class ResourceFileController {
	@Autowired
	private ResourceEntryService resourceEntryService;
	@Autowired
	private ResourceFileRepository resourceFileRepository;
	@Autowired
	private HistoryRepository historyRepository;
	@Autowired
	private SecurityService securityService;

	private final StorageService storageService;
	private final String uploadPath = "/uploadedResourceFiles";
	
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	public ResourceFileController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/resourceEntries/{id:.+}/files")
	public ResponseEntity<String> getFiles(@PathVariable String id, HttpServletRequest request) throws IOException {
		Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(id));
		if (entry.isEmpty())
			return new ResponseEntity<String>("No resource by id found", HttpStatus.NOT_FOUND);
		if (resourceEntryService.canRead(entry.get(),DataController.getJWTCookie(request.getCookies())) || resourceEntryService.canEdit(entry.get(),DataController.getJWTCookie(request.getCookies()))) {
			return new ResponseEntity<String>(mapper.writeValueAsString(entry.get().getFiles()), HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("Not allowed to read resource entry files", HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/resourceEntries/{eid:.+}/files/{id:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String id, @PathVariable String eid, HttpServletRequest request) throws ResourceEntryNotFoundException, ResourceEntryNoAccessException, BadResourceFileForEntryException {
		Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(eid));
		if (entry.isEmpty())
			throw new ResourceEntryNotFoundException();
		if (!resourceEntryService.canRead(entry.get(),DataController.getJWTCookie(request.getCookies())) && !resourceEntryService.canEdit(entry.get(),DataController.getJWTCookie(request.getCookies()))) {
			throw new ResourceEntryNoAccessException();
		}
		Optional<ResourceFile> file = entry.get().getFiles().stream().filter(x -> x.getId() == Long.parseLong(id)).findFirst();
		if (file.isEmpty()) {
			throw new BadResourceFileForEntryException();
		}
		Resource actualFile = storageService.loadAsResource(file.get().getData());
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + actualFile.getFilename() + "\"").contentType(MediaType.parseMediaType("application/octet-stream")).body(actualFile);
	}

	@GetMapping("/resourceEntries/{eid:.+}/files/name/{name:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFileByName(@PathVariable String name, @PathVariable String eid, HttpServletRequest request) throws ResourceEntryNotFoundException, ResourceEntryNoAccessException, BadResourceFileForEntryException {
		Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(eid));
		if (entry.isEmpty())
			throw new ResourceEntryNotFoundException();
		if (!resourceEntryService.canRead(entry.get(),DataController.getJWTCookie(request.getCookies())) && !resourceEntryService.canEdit(entry.get(),DataController.getJWTCookie(request.getCookies()))) {
			throw new ResourceEntryNoAccessException();
		}
		Optional<ResourceFile> file = entry.get().getFiles().stream().filter(x -> x.getFileName().equals(name)).findFirst();
		if (file.isEmpty()) {
			throw new BadResourceFileForEntryException();
		}
		Resource actualFile = storageService.loadAsResource(file.get().getData());
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + actualFile.getFilename() + "\"").contentType(MediaType.parseMediaType("application/octet-stream")).body(actualFile);
	}
	
	@ExceptionHandler(BadResourceFileForEntryException.class)
	public ResponseEntity<String> handleResourceFileNotForEntry(BadResourceFileForEntryException exc){
		return new ResponseEntity<String>("No resource file by id found for entity", HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ResourceEntryNotFoundException.class)
	public ResponseEntity<String> handleStorageFileNotFound(ResourceEntryNotFoundException exc) {
		return new ResponseEntity<String>("No resource by id found", HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(ResourceEntryNoAccessException.class)
	public ResponseEntity<String> handleStorageFileNotFound(ResourceEntryNoAccessException exc) {
		return new ResponseEntity<String>("Not allowed to read resource entry files", HttpStatus.FORBIDDEN);
	}
	

	@PostMapping("/resourceEntries/{id:.+}/files")
	public ResponseEntity<String> handleFileUpload(@PathVariable String id, @RequestParam("file") MultipartFile[] files,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
		Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(id));
		if (entry.isEmpty())
			throw new ResourceEntryNotFoundException();
		if (!resourceEntryService.canEdit(entry.get(),DataController.getJWTCookie(request.getCookies()))) {
			throw new ResourceEntryNoAccessException();
		}
		try {
			for (MultipartFile file : files) {
				if (file.getOriginalFilename().indexOf(".exe") == -1) {
					ResourceFile fileEntry = new ResourceFile();
					fileEntry.setFileName(file.getOriginalFilename());
					fileEntry.setFilePath(request.getServletContext().getRealPath(uploadPath)+"/"+entry.get().getId()+"/"+file.getOriginalFilename());
					entry.get().getFiles().add(fileEntry);
					fileEntry.setData(file.getBytes());
					fileEntry.setSize(file.getSize()+"");
					fileEntry.setType(file.getOriginalFilename().split(".").length > 1 ? file.getOriginalFilename().split(".")[1] : "");
					resourceFileRepository.save(fileEntry);
					History edited = new History();
					edited.setAction("Added resource file");
					edited.setDate(new Date());
					edited.setUserName(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies())));
					historyRepository.save(edited);
					entry.get().getHistories().add(edited);
					resourceEntryService.save(entry.get());
				}
			}
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("File uploaded", HttpStatus.OK);
	}
	
	@DeleteMapping("/resourceEntries/{id:.+}/files")
	public ResponseEntity<String> handleFilesDelete(@PathVariable String id, RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
		Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(id));
		if (entry.isEmpty())
			throw new ResourceEntryNotFoundException();
		if (!resourceEntryService.canEdit(entry.get(),DataController.getJWTCookie(request.getCookies()))) {
			throw new ResourceEntryNoAccessException();
		}
		try {
			entry.get().getFiles().clear();
			for (ResourceFile resFile : entry.get().getFiles()) {
				resourceFileRepository.delete(resFile);
			}
			History edited = new History();
			edited.setAction("Removed resource files");
			edited.setDate(new Date());
			edited.setUserName(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies())));
			historyRepository.save(edited);
			entry.get().getHistories().add(edited);
			resourceEntryService.save(entry.get());
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("Files in file resource entry deleted", HttpStatus.OK);
	}
	
	@DeleteMapping("/resourceEntries/{eid:.+}/files/{id:.+}")
	public ResponseEntity<String> handleFileDelete(@PathVariable String id, @PathVariable String eid,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
		Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(eid));
		if (entry.isEmpty())
			throw new ResourceEntryNotFoundException();
		if (!resourceEntryService.canEdit(entry.get(),DataController.getJWTCookie(request.getCookies()))) {
			throw new ResourceEntryNoAccessException();
		}
		Optional<ResourceFile> file = entry.get().getFiles().stream().filter(x -> x.getId() == Long.parseLong(id)).findFirst();
		if (file.isEmpty()) {
			throw new BadResourceFileForEntryException();
		}
		try {
			entry.get().getFiles().remove(file.get());
			resourceFileRepository.delete(file.get());
			History edited = new History();
			edited.setAction("Removed resource file");
			edited.setDate(new Date());
			edited.setUserName(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies())));
			historyRepository.save(edited);
			entry.get().getHistories().add(edited);
			resourceEntryService.save(entry.get());
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("File deleted", HttpStatus.OK);
	}
	
	

}
