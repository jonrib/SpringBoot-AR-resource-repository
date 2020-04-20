package com.jonrib.tasks.web;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonrib.tasks.model.Role;
import com.jonrib.tasks.model.User;
import com.jonrib.tasks.service.RoleService;
import com.jonrib.tasks.service.SecurityService;
import com.jonrib.tasks.service.UserService;

@RestController
public class RoleController {
	@Autowired
	private RoleService roleService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SecurityService securityService;
	
	private ObjectMapper mapper = new ObjectMapper();
	
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

	@GetMapping(value = "/roles")
	public ResponseEntity<String> getAllRoles(HttpServletRequest request){
		try {
			if (!isAdmin(request)) {
				return new ResponseEntity<String>("Only for admin.", HttpStatus.FORBIDDEN);
			}
				
			return new ResponseEntity<String>(mapper.writeValueAsString(roleService.findAll()), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = "/roles/*")
	public ResponseEntity<String> getRole(HttpServletRequest request){
		try {
			if (!isAdmin(request)) {
				return new ResponseEntity<String>("Only for admin.", HttpStatus.FORBIDDEN);
			}
			
			Role role = roleService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			if (role == null)
				throw new Exception("Role not found");
			return new ResponseEntity<String>(mapper.writeValueAsString(role), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/roles")
	public ResponseEntity<String> postRole(@RequestBody String roleJson, HttpServletRequest request){
		if (!isAdmin(request)) {
			return new ResponseEntity<String>("Only for admin.", HttpStatus.FORBIDDEN);
		}
		
		try {
			Role newRole = mapper.readValue(roleJson, Role.class);
			if (roleService.findByName(newRole.getName()) != null)
				return new ResponseEntity<String>("Role already exists", HttpStatus.BAD_REQUEST);
			roleService.save(newRole);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@DeleteMapping(value = "/roles/*")
	public ResponseEntity<String> deleteRole(HttpServletRequest request){
		if (!isAdmin(request)) {
			return new ResponseEntity<String>("Only for admin.", HttpStatus.FORBIDDEN);
		}
		
		try {
			Role role = roleService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			roleService.delete(role);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
	}
}
