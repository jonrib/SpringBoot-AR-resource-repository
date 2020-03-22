package com.jonrib.tasks.web;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonrib.tasks.model.Task;
import com.jonrib.tasks.model.User;
import com.jonrib.tasks.service.SecurityService;
import com.jonrib.tasks.service.TaskService;
import com.jonrib.tasks.service.UserService;

@Controller
public class TaskController {
	@Autowired
	private TaskService taskService;
	@Autowired
	private UserService userService;
	@Autowired
	private SecurityService securityService;	
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@GetMapping(value = "/tasks/*")
	public ResponseEntity<String> getEntry(HttpServletRequest request){
		try {
			String[] usersStrings = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length()).split(";");
			List<User> users = new ArrayList<User>();
			for (String user : usersStrings) {
				User usr = userService.findByUsername(user);
				if (usr != null)
					users.add(userService.findByUsername(user));
			}
			return new ResponseEntity<String>(mapper.writeValueAsString(taskService.findByUserIn(users)), HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping(value = "/tasks/{id:.+}/{sid:.+}")
	public ResponseEntity<String> getEntryById(HttpServletRequest request, @PathVariable String id, @PathVariable String sid){
		try {
			String[] usersStrings = id.split(";");
			User user = null;
			for (String usr : usersStrings) {
				User actualUsr = userService.findByUsername(usr);
				if (actualUsr != null)
					user=actualUsr;
			}
			if (user == null) {
				return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
			}
			Optional<Task> entry = taskService.findById(Long.parseLong(sid));
			if (entry.isEmpty()) {
				return new ResponseEntity<String>("Entry not found", HttpStatus.NOT_FOUND);
			}
			if (!entry.get().getUsers().contains(user) || !user.equals(userService.findByUsername(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies()))))) {
				return new ResponseEntity<String>("Can't see someone else's task", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<String>(mapper.writeValueAsString(entry.get()), HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}
	@PutMapping(value = "/tasks/{id:.+}/{sid:.+}")
	public ResponseEntity<String> updateEntryById(HttpServletRequest request, @PathVariable String id, @PathVariable String sid, @RequestBody String entryJson){
		try {
			String[] usersStrings = id.split(";");
			User user = null;
			for (String usr : usersStrings) {
				User actualUsr = userService.findByUsername(usr);
				if (actualUsr != null)
					user=actualUsr;
			}
			if (user == null) {
				return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
			}
			Optional<Task> entry = taskService.findById(Long.parseLong(sid));
			if (entry.isEmpty()) {
				return new ResponseEntity<String>("Entry not found", HttpStatus.NOT_FOUND);
			}
			if (!entry.get().getUsers().contains(user) || !user.equals(userService.findByUsername(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies()))))) {
				return new ResponseEntity<String>("Can't see someone else's task", HttpStatus.BAD_REQUEST);
			}
			Task currEntry = entry.get();
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
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}

}
