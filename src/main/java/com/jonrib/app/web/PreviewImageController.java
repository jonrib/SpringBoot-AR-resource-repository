package com.jonrib.app.web;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import com.jonrib.app.model.PreviewImage;
import com.jonrib.app.model.ResourceEntry;
import com.jonrib.app.repository.HistoryRepository;
import com.jonrib.app.repository.PreviewImageRepository;
import com.jonrib.app.service.ResourceEntryService;
import com.jonrib.app.service.SecurityService;
import com.jonrib.app.service.StorageService;

@Controller
public class PreviewImageController {
	@Autowired
	private ResourceEntryService resourceEntryService;
	@Autowired
	private PreviewImageRepository previewImageRepository;
	@Autowired
	private HistoryRepository historyRepository;
	@Autowired
	private SecurityService securityService;

	private final StorageService storageService;
	private final String uploadPath = "/uploadedPreviewImages";
	
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	public PreviewImageController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/resourceEntries/{id:.+}/previewImages")
	public ResponseEntity<String> getFiles(@PathVariable String id, HttpServletRequest request) throws IOException {
		Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(id));
		if (entry.isEmpty())
			return new ResponseEntity<String>("No resource by id found", HttpStatus.NOT_FOUND);
		if (resourceEntryService.canRead(entry.get(),DataController.getJWTCookie(request.getCookies())) || resourceEntryService.canEdit(entry.get(),DataController.getJWTCookie(request.getCookies()))) {
			return new ResponseEntity<String>(mapper.writeValueAsString(entry.get().getImages()), HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("Not allowed to read resource entry files", HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/resourceEntries/{eid:.+}/previewImages/{id:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String id, @PathVariable String eid, HttpServletRequest request) throws ResourceEntryNotFoundException, ResourceEntryNoAccessException, BadResourceFileForEntryException {
		Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(eid));
		if (entry.isEmpty())
			throw new ResourceEntryNotFoundException();
		if (!resourceEntryService.canRead(entry.get(),DataController.getJWTCookie(request.getCookies())) && !resourceEntryService.canEdit(entry.get(),DataController.getJWTCookie(request.getCookies()))) {
			throw new ResourceEntryNoAccessException();
		}
		Optional<PreviewImage> file = entry.get().getImages().stream().filter(x -> x.getId() == Long.parseLong(id)).findFirst();
		if (file.isEmpty()) {
			throw new BadResourceFileForEntryException();
		}
		Resource actualFile = storageService.loadAsResource(file.get().getFilePath());
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + actualFile.getFilename() + "\"").body(actualFile);
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
	

	@PostMapping("/resourceEntries/{id:.+}/previewImages")
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
				PreviewImage fileEntry = new PreviewImage();
				fileEntry.setFileName(file.getOriginalFilename());
				fileEntry.setFilePath(request.getServletContext().getRealPath(uploadPath)+"/"+entry.get().getId()+"/"+file.getOriginalFilename());
				entry.get().getImages().add(fileEntry);
				storageService.store(file, request.getServletContext().getRealPath(uploadPath)+"/"+entry.get().getId());
				fileEntry.setSize(file.getSize()+"");
				previewImageRepository.save(fileEntry);
				History edited = new History();
				edited.setAction("Added preview image file");
				edited.setDate(new Date());
				edited.setUserName(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies())));
				historyRepository.save(edited);
				entry.get().getHistories().add(edited);
				resourceEntryService.save(entry.get());
			}
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("File uploaded", HttpStatus.OK);
	}
	
	@DeleteMapping("/resourceEntries/{id:.+}/previewImages")
	public ResponseEntity<String> handleFilesDelete(@PathVariable String id, RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
		Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(id));
		if (entry.isEmpty())
			throw new ResourceEntryNotFoundException();
		if (!resourceEntryService.canEdit(entry.get(),DataController.getJWTCookie(request.getCookies()))) {
			throw new ResourceEntryNoAccessException();
		}
		try {
			entry.get().getFiles().clear();
			storageService.deleteAll(request.getServletContext().getRealPath(uploadPath));
			for (PreviewImage resFile : entry.get().getImages()) {
				previewImageRepository.delete(resFile);
			}
			History edited = new History();
			edited.setAction("Removed preview image files");
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
	
	@DeleteMapping("/resourceEntries/{eid:.+}/previewImages/{id:.+}")
	public ResponseEntity<String> handleFileDelete(@PathVariable String id, @PathVariable String eid,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
		Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(eid));
		if (entry.isEmpty())
			throw new ResourceEntryNotFoundException();
		if (!resourceEntryService.canEdit(entry.get(),DataController.getJWTCookie(request.getCookies()))) {
			throw new ResourceEntryNoAccessException();
		}
		Optional<PreviewImage> file = entry.get().getImages().stream().filter(x -> x.getId() == Long.parseLong(id)).findFirst();
		if (file.isEmpty()) {
			throw new BadResourceFileForEntryException();
		}
		try {
			entry.get().getImages().remove(file.get());
			storageService.delete(file.get().getFilePath());
			previewImageRepository.delete(file.get());
			History edited = new History();
			edited.setAction("Removed preview image file");
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
