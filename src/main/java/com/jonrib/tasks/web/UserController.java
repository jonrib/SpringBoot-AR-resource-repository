package com.jonrib.tasks.web;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonrib.tasks.jwt.JwtTokenUtil;
import com.jonrib.tasks.model.Role;
import com.jonrib.tasks.model.User;
import com.jonrib.tasks.service.RoleService;
import com.jonrib.tasks.service.SecurityService;
import com.jonrib.tasks.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private RoleService roleService;
	
	@Autowired
	SecurityService securityService;

	private boolean isAdmin(HttpServletRequest request) {
		String username = securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies()));
		boolean isAdmin = false;
		if (username.equals("anonymousUser")) {
			return false;
		}else {
			User user = userService.findByUsername(username);
			for (Role role : user.getRoles()) {
				if (role.getName().equals("Admin")) {
					isAdmin = true;
					break;
				}
			}
		}
		return isAdmin;
	}

	@GetMapping(value = "/users")
	public ResponseEntity<String> getAllUsers(){
		try {
			return new ResponseEntity<String>(mapper.writeValueAsString(userService.findAll()), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/users/*")
	public ResponseEntity<String> getUser(HttpServletRequest request){
		try {
			User user = userService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			if (user == null)
				return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
			return new ResponseEntity<String>(mapper.writeValueAsString(user), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/users")
	public ResponseEntity<String> postUser(@RequestBody String userJson){
		try {
			User newUser = mapper.readValue(userJson, User.class);
			
			newUser.setRoles(new HashSet<Role>());
			
			if (userService.findByUsername(newUser.getUsername()) != null)
				return new ResponseEntity<String>("Already exists", HttpStatus.BAD_REQUEST);
			
			if (newUser.getUsername().length() < 6 || newUser.getUsername().length() > 32) {
				return new ResponseEntity<String>("Username size is incorrect.", HttpStatus.BAD_REQUEST);
	        }
			
	        if (newUser.getPassword().length() < 8 || newUser.getPassword().length() > 32) {
	        	return new ResponseEntity<String>("Password size is incorrect.", HttpStatus.BAD_REQUEST);
	        }

	        if (!newUser.getPasswordConfirm().equals(newUser.getPassword())) {
	        	return new ResponseEntity<String>("Confirm password is incorrect.", HttpStatus.BAD_REQUEST);
	        }

			userService.save(newUser);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@DeleteMapping(value = "/users/*")
	public ResponseEntity<String> deleteUser(HttpServletRequest request){
		try {
			if (!isAdmin(request)) {
				return new ResponseEntity<String>("Only for admin.", HttpStatus.FORBIDDEN);
			}
			
			User usr = userService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			usr.setRoles(null);
			
			userService.delete(usr);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value = "/users/*")
	public ResponseEntity<String> putUser(HttpServletRequest request, @RequestBody String userJson){
		try {
			if (!isAdmin(request)) {
				return new ResponseEntity<String>("Only for admin.", HttpStatus.FORBIDDEN);
			}
			
			User user = userService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			if (user == null)
				throw new Exception("User not found");
			JsonNode root = mapper.readTree(userJson);
			Set<Role> roles = new HashSet<Role>();
			for (Iterator<JsonNode> i = root.at("/users/roles").elements(); i.hasNext();) {
				String name = i.next().asText();
				Role role = roleService.findByName(name);
				if (role == null)
					throw new Exception("Role not found " + name);
				roles.add(role);
			}
			user.setRoles(roles);
			userService.update(user);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}

}
