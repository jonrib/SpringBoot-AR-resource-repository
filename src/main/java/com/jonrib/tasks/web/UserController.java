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
import com.jonrib.tasks.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private RoleService roleService;

	private JwtTokenUtil util = new JwtTokenUtil();



	@GetMapping(value = "/users")
	public ResponseEntity<String> getAllUsers(){
		try {
			return new ResponseEntity<String>(mapper.writeValueAsString(userService.findAll()), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = "/users/*")
	public ResponseEntity<String> getUser(HttpServletRequest request){
		try {
			User user = userService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			if (user == null)
				throw new Exception("User not found");
			return new ResponseEntity<String>(mapper.writeValueAsString(user), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/users")
	public ResponseEntity<String> postUser(@RequestBody String userJson){
		try {
			User newUser = mapper.readValue(userJson, User.class);
			if (userService.findByUsername(newUser.getUsername()) != null)
				throw new Exception("User already exists");

			userService.save(newUser);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@DeleteMapping(value = "/users/*")
	public ResponseEntity<String> deleteUser(HttpServletRequest request){
		try {
			Cookie[] cookies = request.getCookies();
			String userName = "";
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("JWT")) {
					userName = cookies[i].getValue().replace("Bearer", "");
					System.out.println("jwt cookie " + userName);
				}
			}

			User curr = userService.findByUsername(util.getUsernameFromToken(userName));
			boolean auth = false;
			for (Role role : curr.getRoles()) {
				if (role.getName().equals("admin")) {
					auth= true;
				}
			}
			if (!auth) {
				return new ResponseEntity<String>("Unauthorized", HttpStatus.FORBIDDEN);
			}
			User usr = userService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			usr.setRoles(null);


			userService.delete(usr);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = "/users/*")
	public ResponseEntity<String> patchUser(HttpServletRequest request, @RequestBody String userJson){
		try {
			User user = userService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			if (user == null)
				throw new Exception("User not found");
			//User newUser = mapper.readValue(userJson, User.class);
			JsonNode root = mapper.readTree(userJson);
			//User newUser = mapper.treeToValue(root.at("/data"), User.class);
			Set<Role> roles = new HashSet<Role>();
			for (Iterator<JsonNode> i = root.at("/users/roles").elements(); i.hasNext();) {
				String name = i.next().asText();
				Role role = roleService.findByName(name);
				if (role == null)
					throw new Exception("Role not found " + name);
				roles.add(role);
			}
			//user.setGroup(newUser.getGroup());
			//user.setProjects(newUser.getProjects());
			//user.setTasks(newUser.getTasks());
			user.setRoles(roles);
			//user.setPassword(newUser.getPassword());
			//user.setPasswordConfirm(newUser.getPasswordConfirm());
			//user.setUsername(newUser.getUsername());
			userService.update(user);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
		}
	}

}
