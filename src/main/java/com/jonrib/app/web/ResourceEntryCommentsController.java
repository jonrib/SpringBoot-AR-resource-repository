package com.jonrib.app.web;

import java.util.Date;
import java.util.HashSet;
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
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonrib.app.model.Comment;
import com.jonrib.app.model.ResourceEntry;
import com.jonrib.app.model.Role;
import com.jonrib.app.model.User;
import com.jonrib.app.service.CommentService;
import com.jonrib.app.service.ResourceEntryService;
import com.jonrib.app.service.SecurityService;
import com.jonrib.app.service.UserService;

@Controller
public class ResourceEntryCommentsController {
	@Autowired
	private SecurityService securityService;
	@Autowired
	private ResourceEntryService resourceEntryService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private UserService userService;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@GetMapping(value = "/resourceEntries/{id:.+}/comments")
	public ResponseEntity<String> getAllEntries(@PathVariable String id, HttpServletRequest request){
		try {
			Optional<ResourceEntry> entry = resourceEntryService.findById(Long.parseLong(id));
			if (entry.isEmpty()) {
				return new ResponseEntity<String>("Resource entry not found", HttpStatus.NOT_FOUND);
			}
			if (!resourceEntryService.canEdit(entry.get(),DataController.getJWTCookie(request.getCookies())) && !resourceEntryService.canRead(entry.get(),DataController.getJWTCookie(request.getCookies()))) {
				return new ResponseEntity<String>("Can't read resource entry", HttpStatus.FORBIDDEN);
			}
			return new ResponseEntity<String>(mapper.writeValueAsString(entry.get().getComments()), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping(value = "/resourceEntries/{id:.+}/comments/*")
	public ResponseEntity<String> deleteEntry(@PathVariable String id, HttpServletRequest request){
		try {
			Optional<Comment> entry = commentService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			Optional<ResourceEntry> resEntry = resourceEntryService.findById(Long.parseLong(id));
			if (entry.isEmpty())
				return new ResponseEntity<String>("Entry not found", HttpStatus.NOT_FOUND);
			if (resEntry.isEmpty()) {
				return new ResponseEntity<String>("Resource entry not found", HttpStatus.NOT_FOUND);
			}
			User user = userService.findByUsername(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies())));
			Set<Role> userRoles = user != null ? user.getRoles() : new HashSet<Role>();
			boolean isAdmin = false;
			for (Role role : userRoles) {
				if (role.getName().equals("Admin")) {
					isAdmin = true;
					break;
				}
			}
			if (!isAdmin && (!securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies())).equals("") || !entry.get().getUserName().equals(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies())))))
				return new ResponseEntity<String>("Only author or admin can delete comment", HttpStatus.FORBIDDEN);
			resourceEntryService.save(resEntry.get());
			commentService.delete(entry.get());
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/resourceEntries/{id:.+}/comments")
	public ResponseEntity<String> postEntry(@PathVariable String id, HttpServletRequest request, @RequestBody String entryJson){
		try {
			Optional<ResourceEntry> resEntry = resourceEntryService.findById(Long.parseLong(id));
			if (resEntry.isEmpty()) {
				return new ResponseEntity<String>("Resource entry not found", HttpStatus.NOT_FOUND);
			}
			if (!resourceEntryService.canEdit(resEntry.get(),DataController.getJWTCookie(request.getCookies())) && !resourceEntryService.canRead(resEntry.get(),DataController.getJWTCookie(request.getCookies()))) {
				return new ResponseEntity<String>("Can't read resource entry", HttpStatus.FORBIDDEN);
			}
			
			Comment entry = mapper.readValue(entryJson, Comment.class);
			entry.setUserName(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies())));
			entry.setDate(new Date());
			Comment rez = commentService.save(entry);
			entry.setReplies(new HashSet<Comment>());
			resEntry.get().getComments().add(entry);
			resourceEntryService.save(resEntry.get());
			
			return new ResponseEntity<String>(mapper.writeValueAsString(rez), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/resourceEntries/{id:.+}/comments/*")
	public ResponseEntity<String> postEntryReply(@PathVariable String id, HttpServletRequest request, @RequestBody String entryJson){
		try {
			Optional<ResourceEntry> resEntry = resourceEntryService.findById(Long.parseLong(id));
			Optional<Comment> entry = commentService.findById(Long.parseLong(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length())));
			if (entry.isEmpty())
				return new ResponseEntity<String>("Entry not found", HttpStatus.NOT_FOUND);
			if (resEntry.isEmpty()) {
				return new ResponseEntity<String>("Resource entry not found", HttpStatus.NOT_FOUND);
			}
			if (!resourceEntryService.canEdit(resEntry.get(),DataController.getJWTCookie(request.getCookies())) && !resourceEntryService.canRead(resEntry.get(),DataController.getJWTCookie(request.getCookies()))) {
				return new ResponseEntity<String>("Can't read resource entry", HttpStatus.FORBIDDEN);
			}
			Comment reply = mapper.readValue(entryJson, Comment.class);
			reply.setUserName(securityService.findLoggedInUsername(DataController.getJWTCookie(request.getCookies())));
			reply.setDate(new Date());
			entry.get().getReplies().add(reply);
			
			Comment rez = commentService.save(reply);
			commentService.save(entry.get());
			
			return new ResponseEntity<String>(mapper.writeValueAsString(rez), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}
}
