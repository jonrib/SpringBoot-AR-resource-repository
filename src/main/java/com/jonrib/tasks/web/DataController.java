package com.jonrib.tasks.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class DataController{
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
	 
	 public static String getJWTCookie(Cookie[] cookies) {
		 if (cookies == null)
			 return "";
		 for (Cookie cookie : cookies) {
				System.out.println("here " + cookie.getName());
				if (cookie.getName().equals("JWT")) {
					return "Bearer " + cookie.getValue();
				}
			}
		 return "";
	 }
}
