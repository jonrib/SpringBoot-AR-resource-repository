package com.jonrib.tasks.web;

import java.util.Date;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonrib.tasks.model.History;
import com.jonrib.tasks.model.ResourceEntry;
import com.jonrib.tasks.model.Role;
import com.jonrib.tasks.model.User;
import com.jonrib.tasks.repository.HistoryRepository;
import com.jonrib.tasks.model.Task;
import com.jonrib.tasks.service.ResourceEntryService;
import com.jonrib.tasks.service.SecurityService;
import com.jonrib.tasks.service.TaskService;
import com.jonrib.tasks.service.UserService;
@Controller
public class ResourceEntryTasksController {
	@Autowired
	private TaskService taskService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private UserService userService;
	@Autowired
	private ResourceEntryService resourceEntryService;
	@Autowired
	private HistoryRepository historyRepository;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@GetMapping(value = "/resourceEntries/{id:.+}/tasks")
	public ResponseEntity<String> getAllEntries(@PathVariable String id, HttpServletRequest request){
		try {
			Set<Role> userRoles = userService.findByUsername(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies()))).getRoles();
			Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(id));
			if (entry.isEmpty()) {
				return new ResponseEntity<String>("Resource entry not found", HttpStatus.NOT_FOUND);
			}
			boolean canRead = false;
			for (Role role : userRoles) {
				if (role.getName().equals("TasksReader") || role.getName().equals("Admin")) {
					canRead = true;
					break;
				}
			}
			if (!canRead) {
				return new ResponseEntity<String>("User does not have 'TasksReader' role", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<String>(mapper.writeValueAsString(entry.get().getTasks()), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/resourceEntries/{id:.+}/tasks/*")
	public ResponseEntity<String> getEntry(HttpServletRequest request, @PathVariable String id){
		try {
			Optional<Task> entry = taskService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			Optional<ResourceEntry> resEntry = resourceEntryService.findById(Long.parseLong(id));
			if (!resEntry.isPresent())
				return new ResponseEntity<String>("Resource entry not found", HttpStatus.NOT_FOUND);
			if (!resEntry.get().getTasks().contains(entry.get())) {
				new ResponseEntity<String>("Entry not in resource entry", HttpStatus.BAD_REQUEST);
			}
			if (!resourceEntryService.canRead(resEntry.get(),DataController.getJWTCookie(request.getCookies())) && !resourceEntryService.canEdit(resEntry.get(),DataController.getJWTCookie(request.getCookies())))
				return new ResponseEntity<String>("Can't read entry", HttpStatus.BAD_REQUEST);
			
			return new ResponseEntity<String>(mapper.writeValueAsString(entry.get()), HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/resourceEntries/{id:.+}/tasks")
	public ResponseEntity<String> postEntry(@PathVariable String id, @RequestBody String entryJson, HttpServletRequest request){
		try {
			JsonNode root = mapper.readTree(entryJson);
			if (securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies())) == "anonymousUser")
				return new ResponseEntity<String>("You need to be logged in to create entries", HttpStatus.BAD_REQUEST);
			Set<Role> userRoles = userService.findByUsername(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies()))).getRoles();
			boolean canCreate = false;
			for (Role role : userRoles) {
				if (role.getName().equals("TaskCreator") || role.getName().equals("Admin")) {
					canCreate = true;
					break;
				}
			}
			if (!canCreate) {
				return new ResponseEntity<String>("User does not have 'TaskCreator' role", HttpStatus.BAD_REQUEST);
			}
			Optional<ResourceEntry> resEntry = resourceEntryService.findById(Long.parseLong(id));
			if (resEntry.isEmpty()) {
				return new ResponseEntity<String>("Resource entry not found", HttpStatus.NOT_FOUND);
			}
			if (!resourceEntryService.canEdit(resEntry.get(),DataController.getJWTCookie(request.getCookies()))) {
				return new ResponseEntity<String>("User can't edit resource entry", HttpStatus.BAD_REQUEST);
			}
			Task newEntry = mapper.treeToValue(root.at("/entry"), Task.class);
			Set<User> author = new HashSet<User>();
			author.add(userService.findByUsername(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies()))));
			newEntry.setAuthor(author);
			Set<User> users = new HashSet<User>();
			for (Iterator<JsonNode> i = root.at("/users").elements(); i.hasNext();) {
				String username = i.next().asText();
				User usr = userService.findByUsername(username);
				if (usr != null)
					users.add(usr);
			}
			newEntry.setUsers(users);
			newEntry.setUsersDone(new HashSet<User>());
			taskService.save(newEntry);
			resEntry.get().getTasks().add(newEntry);
			History edited = new History();
			edited.setAction("Created task");
			edited.setDate(new Date());
			edited.setUserName(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies())));
			historyRepository.save(edited);
			resEntry.get().getHistories().add(edited);
			resourceEntryService.save(resEntry.get());
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@DeleteMapping(value = "/resourceEntries/{id:.+}/tasks/*")
	public ResponseEntity<String> deleteEntry(@PathVariable String id, HttpServletRequest request){
		try {
			Optional<Task> entry = taskService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			Optional<ResourceEntry> resEntry = resourceEntryService.findById(Long.parseLong(id));
			if (entry.isEmpty())
				return new ResponseEntity<String>("Entry not found", HttpStatus.NOT_FOUND);
			if (resEntry.isEmpty()) {
				return new ResponseEntity<String>("Resource entry not found", HttpStatus.NOT_FOUND);
			}
			if (!resourceEntryService.canEdit(resEntry.get(),DataController.getJWTCookie(request.getCookies()))) {
				return new ResponseEntity<String>("User can't edit resource entry", HttpStatus.BAD_REQUEST);
			}
			Set<Role> userRoles = userService.findByUsername(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies()))).getRoles();
			boolean canDelete = false;
			for (Role role : userRoles) {
				if (role.getName().equals("TaskDeletor") || role.getName().equals("Admin")) {
					canDelete = true;
					break;
				}
			}
			if (!canDelete) {
				return new ResponseEntity<String>("User does not have 'TaskDeletor' role", HttpStatus.BAD_REQUEST);
			}
			
			taskService.delete(entry.get());
			resEntry.get().getTasks().remove(entry.get());
			History edited = new History();
			edited.setAction("Removed task");
			edited.setDate(new Date());
			edited.setUserName(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies())));
			historyRepository.save(edited);
			resEntry.get().getHistories().add(edited);
			resourceEntryService.save(resEntry.get());
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value = "/resourceEntries/{id:.+}/tasks/*")
	public ResponseEntity<String> patchEntry(@PathVariable String id, HttpServletRequest request, @RequestBody String entryJson){
		try {
			Optional<Task> entry = taskService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			Optional<ResourceEntry> resEntry = resourceEntryService.findById(Long.parseLong(id));
			if (entry.isEmpty())
				return new ResponseEntity<String>("Entry not found", HttpStatus.NOT_FOUND);
			if (resEntry.isEmpty()) {
				return new ResponseEntity<String>("Resource entry not found", HttpStatus.NOT_FOUND);
			}
			if (resourceEntryService.canEdit(resEntry.get(),DataController.getJWTCookie(request.getCookies()))) {
				return new ResponseEntity<String>("User can't edit resource entry", HttpStatus.BAD_REQUEST);
			}
			Task currEntry = entry.get();
			resEntry.get().getTasks().remove(entry.get());
			JsonNode root = mapper.readTree(entryJson);
			Task newEntry = mapper.treeToValue(root.at("/entry"), Task.class);
			currEntry.setDescription(newEntry.getDescription());
			currEntry.setTitle(newEntry.getTitle());
			Set<User> users = new HashSet<User>();
			for (Iterator<JsonNode> i = root.at("/users").elements(); i.hasNext();) {
				String username = i.next().asText();
				User usr = userService.findByUsername(username);
				if (usr != null)
					users.add(usr);
			}
			Set<User> usersDone = new HashSet<User>();
			for (Iterator<JsonNode> i = root.at("/usersDone").elements(); i.hasNext();) {
				String username = i.next().asText();
				User usr = userService.findByUsername(username);
				if (usr != null)
					usersDone.add(usr);
			}
			currEntry.setUsers(users);
			currEntry.setUsersDone(usersDone);
			currEntry.setDueDate(newEntry.getDueDate());
			for (User userDone : users) {
				if (!users.contains(userDone)){
					return new ResponseEntity<String>("User completing task not in users assigned to task", HttpStatus.BAD_REQUEST);
				}
			}
			if (currEntry.getUsers().size() == currEntry.getUsersDone().size()) {
				currEntry.setDoneDate(new Date());
				currEntry.setDone(true);
			}
			
			taskService.save(currEntry);
			resEntry.get().getTasks().add(currEntry);
			History edited = new History();
			edited.setAction("Edited task");
			edited.setDate(new Date());
			edited.setUserName(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies())));
			historyRepository.save(edited);
			resEntry.get().getHistories().add(edited);
			resourceEntryService.save(resEntry.get());
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}
}
