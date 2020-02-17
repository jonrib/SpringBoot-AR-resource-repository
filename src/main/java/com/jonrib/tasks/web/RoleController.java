package com.jonrib.tasks.web;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonrib.tasks.model.Role;
import com.jonrib.tasks.model.User;
import com.jonrib.tasks.service.RoleService;
import com.jonrib.tasks.service.UserService;

@RestController
public class RoleController {
	@Autowired
	private RoleService roleService;
	
	@Autowired
	UserService userService;
	
	private ObjectMapper mapper = new ObjectMapper();

	@GetMapping(value = "/roles")
	public ResponseEntity<String> getAllTasks(){
		try {
			return new ResponseEntity<String>(mapper.writeValueAsString(roleService.findAll()), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = "/roles/*")
	public ResponseEntity<String> getProject(HttpServletRequest request){
		try {
			Role role = roleService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			if (role == null)
				throw new Exception("Role not found");
			return new ResponseEntity<String>(mapper.writeValueAsString(role), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/roles")
	public ResponseEntity<String> postProject(@RequestBody String roleJson){
		try {
			//JsonNode root = mapper.readTree(roleJson);
			Role newRole = mapper.readValue(roleJson, Role.class);
			if (roleService.findByName(newRole.getName()) != null)
				 throw new Exception("Role already exists");
			/*
			Set<User> users = new HashSet<User>();
			for (Iterator<JsonNode> i = root.at("/users/users").elements(); i.hasNext();) {
				String username = i.next().asText();
				User usr = userService.findByUsername(username);
				if (usr == null)
					throw new Exception("User not found " + username);
				users.add(usr);
			 }
			 */
			//newRole.setUsers(users);
			roleService.save(newRole);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@DeleteMapping(value = "/roles/*")
	public ResponseEntity<String> deleteProject(HttpServletRequest request){
		try {
			Role role = roleService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			roleService.delete(role);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = "/roles/*")
	public ResponseEntity<String> patchProject(HttpServletRequest request, @RequestBody String taskJson){
		try {
			Role role = roleService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			if (role == null)
				throw new Exception("Role not found");
			Role newRole = mapper.readValue(taskJson, Role.class);
			/*
			Role newRole = mapper.treeToValue(root.at("/data"), Role.class);
			Set<User> users = new HashSet<User>();
			for (Iterator<JsonNode> i = root.at("/users/users").elements(); i.hasNext();) {
				String username = i.next().asText();
				User usr = userService.findByUsername(username);
				if (usr == null)
					throw new Exception("User not found " + username);
				users.add(usr);
			 }
			role.setName(newRole.getName());
			*/
			//role.setUsers(users);
			roleService.save(role);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
	}
}
