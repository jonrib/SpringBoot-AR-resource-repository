package com.jonrib.app.web;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonrib.app.model.ResourceEntry;
import com.jonrib.app.model.User;
import com.jonrib.app.service.UserService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Controller
public class DataController{
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private UserService userService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@DeleteMapping(value = "/deleteJWT")
	public ResponseEntity<String> deleteJWT(HttpServletResponse response){
		Cookie cookie = new Cookie("JWT", null);
		cookie.setMaxAge(0);
		cookie.setHttpOnly(true);
		cookie.setPath("/"); // global cookie accessible every where
		//cookie.setSecure(true);
		response.addCookie(cookie);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	public static String getBearerTokenHeader() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
	}

	public static String getJWTCookie(Cookie[] cookies) {
		String header = getBearerTokenHeader();
		if (header != null && !header.equals("")){
			return header;
		}

		if (cookies == null)
			return "";
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("JWT")) {
				return "Bearer " + cookie.getValue();
			}
		}
		return "";
	}
	@GetMapping(value="/user/{id:.+}")
	public ResponseEntity<String> getUser(@PathVariable String id, HttpServletRequest request) throws IOException {
		User entry = userService.findByUsername(id);
		if (entry == null)
			return new ResponseEntity<String>("No user found", HttpStatus.NOT_FOUND);
		return new ResponseEntity<String>(mapper.writeValueAsString(entry), HttpStatus.OK);
	}
	@PutMapping(value="/user/{id:.+}")
	public ResponseEntity<String> putUser(@PathVariable String id, HttpServletRequest request, @RequestBody String entryJson) throws IOException {
		User updatedUser = mapper.readValue(entryJson, User.class);
		User entry = userService.findByUsername(id);
		if (entry == null)
			return new ResponseEntity<String>("No user found", HttpStatus.NOT_FOUND);
		try {
		UserDetails userDetails = userDetailsService.loadUserByUsername(id);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, updatedUser.getPasswordConfirm(), userDetails.getAuthorities());        
		authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		if (!usernamePasswordAuthenticationToken.isAuthenticated()) {
			return new ResponseEntity<String>("Incorrect old password", HttpStatus.BAD_REQUEST);
		}
		}catch(BadCredentialsException e) {
			return new ResponseEntity<String>("Incorrect old password", HttpStatus.BAD_REQUEST);
		}
		
		entry.setEmail(updatedUser.getEmail());
		entry.setPassword(updatedUser.getPassword());
		userService.save(entry);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
}
