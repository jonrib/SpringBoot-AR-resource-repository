package com.jonrib.tasks.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DataController{
	 @DeleteMapping(value = "/deleteJWT")
	 public ResponseEntity<String> deleteJWT(HttpServletResponse response){
		 Cookie cookie = new Cookie("JWT", null);
		 cookie.setMaxAge(0);
		 cookie.setHttpOnly(true);
		 response.addCookie(cookie);
		 return new ResponseEntity<String>("success", HttpStatus.OK);
	 }
}
