package com.jonrib.tasks.web;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonrib.tasks.exceptions.ResourceEntryNotFoundException;
import com.jonrib.tasks.model.Download;
import com.jonrib.tasks.model.ResourceEntry;
import com.jonrib.tasks.service.DownloadService;
import com.jonrib.tasks.service.ResourceEntryService;

@Controller
public class DownloadController {
	@Autowired
	private ResourceEntryService resourceEntryService;
	@Autowired
	private DownloadService downloadService;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@GetMapping("/resourceEntries/{id:.+}/downloads")
	public ResponseEntity<String> getDownloads(@PathVariable String id, HttpServletRequest request) {
		try {
			Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(id));
			if (entry.isEmpty())
				return new ResponseEntity<String>("No resource by id found", HttpStatus.NOT_FOUND);
			if (resourceEntryService.canRead(entry.get(),DataController.getJWTCookie(request.getCookies())) || resourceEntryService.canEdit(entry.get(), DataController.getJWTCookie(request.getCookies()))) {
				return new ResponseEntity<String>(mapper.writeValueAsString(entry.get().getDownloads()), HttpStatus.OK);
			}else {
				return new ResponseEntity<String>("Not allowed to read resource entry downloads", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/resourceEntries/{id:.+}/downloads")
	public ResponseEntity<String> handleFileDownload(@PathVariable String id, @RequestBody String entryJson,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
		Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(id));
		if (entry.isEmpty())
			throw new ResourceEntryNotFoundException();
		if (!resourceEntryService.canEdit(entry.get(),DataController.getJWTCookie(request.getCookies())) && !resourceEntryService.canRead(entry.get(),DataController.getJWTCookie(request.getCookies()))) {
			return new ResponseEntity<String>("Not allowed to read resource entry downloads", HttpStatus.BAD_REQUEST);
		}
		try {
			Download download = mapper.readValue(entryJson, Download.class);
			entry.get().getDownloads().add(download);
			downloadService.save(download);
			resourceEntryService.save(entry.get());
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("Download entry created", HttpStatus.OK);
	}

}
