package com.jonrib.tasks.web;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonrib.tasks.model.User;
import com.jonrib.tasks.repository.UserRepository;
import com.jonrib.tasks.service.SecurityService;
import com.jonrib.tasks.service.UserService;


@RestController
public class DataController{
	
	 @Autowired
	 private SecurityService securityService;
	 
	 @Autowired
	 private UserService userService;
	 
	 @Autowired
	 private UserRepository userRepository;
	 
	 private ObjectMapper mapper = new ObjectMapper();
	 /*
	 @GetMapping(value = "/getAllUsers")
	 public ResponseEntity<String> getAllUsers(@RequestParam String usernames){
		 String[] allUserNames = usernames.split(",");
		 for (String user : allUserNames) {
			 if (userService.findByUsername(user.trim()) == null)
				 return new ResponseEntity<String>("not found", HttpStatus.NOT_FOUND);
		 }
		 return new ResponseEntity<String>("success", HttpStatus.OK);
	 }
	 */
	 /*
	 @GetMapping(value = "/getTasks")
	 public ResponseEntity<String> getTasks(@RequestParam String usernames) throws JsonProcessingException{
		 User usr = userService.findByUsername(usernames.trim());
		 if (usr != null) {
			 return new ResponseEntity<String>(mapper.writeValueAsString(usr.getTasks()), HttpStatus.OK);
		 }
		 return new ResponseEntity<String>("failed", HttpStatus.OK);
	 }*/
	 @DeleteMapping(value = "/deleteJWT")
	 public ResponseEntity<String> deleteJWT(HttpServletResponse response){
		 Cookie cookie = new Cookie("JWT", null);
		 cookie.setMaxAge(0);
		 cookie.setHttpOnly(true);
		 response.addCookie(cookie);
		 return new ResponseEntity<String>("success", HttpStatus.OK);
	 }
	 /*
	 @PostMapping(value = "/addTaskToUser")
	 public ResponseEntity<String> postTask(@RequestParam String username, @RequestBody String taskJson) throws JsonParseException, JsonMappingException, IOException{
		 User usr = userRepository.findByUsername(username);
		 Task newTask = mapper.readValue(taskJson, Task.class);
		 newTask.getUsers().add(usr);
		 taskRepository.save(newTask);
		 usr.getTasks().add(newTask);
		 userRepository.save(usr);
		 return new ResponseEntity<String>("success", HttpStatus.OK);
	 }*/
	 /*
	 @PostMapping(value = "/addUser")
	 public ResponseEntity<String> addUser(@RequestParam String username, @RequestParam String password){
		 User user = new User();
		 user.setUsername(username);
		 user.setPassword(password);
		 userRepository.save(user);
		 return new ResponseEntity<String>("success", HttpStatus.OK);
	 }*/
	
}
