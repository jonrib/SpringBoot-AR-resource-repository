package com.jonrib.app.web;

import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonrib.app.model.History;
import com.jonrib.app.model.PreviewImage;
import com.jonrib.app.model.ResourceEntry;
import com.jonrib.app.model.ResourceFile;
import com.jonrib.app.model.User;
import com.jonrib.app.repository.HistoryRepository;
import com.jonrib.app.service.ResourceEntryService;
import com.jonrib.app.service.SecurityService;
import com.jonrib.app.service.StorageService;
import com.jonrib.app.service.UserService;

@Controller
public class ResourceEntryController {
	@Autowired
	private ResourceEntryService resourceEntryService;
	@Autowired
	private StorageService storageService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private UserService userService;
	@Autowired
	private HistoryRepository historyRepository;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@GetMapping(value = "/resourceEntries")
	public ResponseEntity<String> getAllEntries(){
		try {
			return new ResponseEntity<String>(mapper.writeValueAsString(resourceEntryService.findAll()), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping(value = "/resourceEntries/category/*")
	public ResponseEntity<String> getAllEntriesForCategory(HttpServletRequest request){
		try {
			List<ResourceEntry> entries = resourceEntryService.findByCategory(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length()));
			return new ResponseEntity<String>(mapper.writeValueAsString(entries), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value = "/resourceEntries/tags/*")
	public ResponseEntity<String> getAllEntriesForTags(HttpServletRequest request){
		try {
			List<String> tags = Arrays.asList(URLDecoder.decode(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length()), java.nio.charset.StandardCharsets.UTF_8.toString()).split(" "));
			List<ResourceEntry> entries = resourceEntryService.findByTagsIn(tags);
			return new ResponseEntity<String>(mapper.writeValueAsString(entries), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = "/resourceEntries/*")
	public ResponseEntity<String> getEntry(HttpServletRequest request){
		try {
			Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			if (!entry.isPresent())
				return new ResponseEntity<String>("Entry not found", HttpStatus.NOT_FOUND);
			if (!resourceEntryService.canRead(entry.get(),DataController.getJWTCookie(request.getCookies())) && !resourceEntryService.canEdit(entry.get(),DataController.getJWTCookie(request.getCookies())))
				return new ResponseEntity<String>("Can't read entry. Contact author at: "+entry.get().getAuthor().stream().findFirst().get().getEmail(), HttpStatus.FORBIDDEN);
			return new ResponseEntity<String>(mapper.writeValueAsString(entry.get()), HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/resourceEntries")
	public ResponseEntity<String> postEntry(@RequestBody String entryJson, HttpServletRequest request){
		try {
			if (securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies())).equals("anonymousUser"))
				return new ResponseEntity<String>("You need to be logged in to create entries", HttpStatus.FORBIDDEN);
			JsonNode root = mapper.readTree(entryJson);
			ResourceEntry newEntry = mapper.treeToValue(root.at("/entry"), ResourceEntry.class);
			Set<User> author = new HashSet<User>();
			author.add(userService.findByUsername(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies()))));
			newEntry.setAuthor(author);
			newEntry.setEditors(new HashSet<User>());
			newEntry.setFiles(new HashSet<ResourceFile>());
			newEntry.setImages(new HashSet<PreviewImage>());
			newEntry.setReaders(new HashSet<User>());
			newEntry.setHistories(new HashSet<History>());
			History created = new History();
			created.setAction("Created");
			created.setDate(new Date());
			created.setUserName(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies())));
			historyRepository.save(created);
			newEntry.getHistories().add(created);
			Set<User> readers = new HashSet<User>();
			for (Iterator<JsonNode> i = root.at("/readers").elements(); i.hasNext();) {
				String username = i.next().asText();
				User usr = userService.findByUsername(username);
				if (usr != null)
					readers.add(usr);
			}
			Set<User> editors = new HashSet<User>();
			for (Iterator<JsonNode> i = root.at("/editors").elements(); i.hasNext();) {
				String username = i.next().asText();
				User usr = userService.findByUsername(username);
				if (usr != null)
					editors.add(usr);
			}
			newEntry.setReaders(readers);
			newEntry.setEditors(editors);
			return new ResponseEntity<String>(mapper.writeValueAsString(resourceEntryService.save(newEntry)), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value = "/resourceEntries/*")
	public ResponseEntity<String> deleteEntry(HttpServletRequest request){
		try {
			Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			if (entry.isEmpty())
				return new ResponseEntity<String>("Entry not found", HttpStatus.NOT_FOUND);
			if (!resourceEntryService.canEdit(entry.get(),DataController.getJWTCookie(request.getCookies())))
				return new ResponseEntity<String>("You're not an editor for entry", HttpStatus.FORBIDDEN);
			resourceEntryService.delete(entry.get());
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value = "/resourceEntries/*")
	public ResponseEntity<String> putEntry(HttpServletRequest request, @RequestBody String entryJson){
		try {
			Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			if (entry.isEmpty())
				return new ResponseEntity<String>("Entry not found", HttpStatus.NOT_FOUND);
			if (!resourceEntryService.canEdit(entry.get(),DataController.getJWTCookie(request.getCookies())))
				return new ResponseEntity<String>("You're not an editor for entry", HttpStatus.FORBIDDEN);
			ResourceEntry currEntry = entry.get();
			JsonNode root = mapper.readTree(entryJson);
			ResourceEntry newEntry = mapper.treeToValue(root.at("/entry"), ResourceEntry.class);
			currEntry.setCategory(newEntry.getCategory());
			currEntry.setDescription(newEntry.getDescription());
			currEntry.setPrivate(newEntry.isPrivate());
			currEntry.setTags(newEntry.getTags());
			currEntry.setTitle(newEntry.getTitle());
			Set<User> readers = new HashSet<User>();
			for (Iterator<JsonNode> i = root.at("/readers").elements(); i.hasNext();) {
				String username = i.next().asText();
				User usr = userService.findByUsername(username);
				if (usr != null)
					readers.add(usr);
			}
			Set<User> editors = new HashSet<User>();
			for (Iterator<JsonNode> i = root.at("/editors").elements(); i.hasNext();) {
				String username = i.next().asText();
				User usr = userService.findByUsername(username);
				if (usr != null)
					editors.add(usr);
			}
			currEntry.setReaders(readers);
			currEntry.setEditors(editors);
			History edited = new History();
			edited.setAction("Edited");
			edited.setDate(new Date());
			edited.setUserName(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies())));
			historyRepository.save(edited);
			currEntry.getHistories().add(edited);
			return new ResponseEntity<String>(mapper.writeValueAsString(resourceEntryService.save(currEntry)), HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}
}
