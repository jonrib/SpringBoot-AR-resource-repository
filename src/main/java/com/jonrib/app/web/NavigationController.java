package com.jonrib.app.web;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.jonrib.app.model.User;
import com.jonrib.app.service.SecurityService;
import com.jonrib.app.service.UserService;


@Controller
public class NavigationController {

	@Autowired
	private SecurityService securityService;

	@PostMapping("/login")
	public ResponseEntity<String> login(Model model, HttpServletResponse response, HttpServletRequest request) {
		String token = null;
		try {
			token = securityService.autoLogin(request.getParameter("username").toString(), request.getParameter("password").toString());
		}catch (Exception e) {
			return new ResponseEntity<String>("Your username and password is invalid.", HttpStatus.BAD_REQUEST);
		}
		Cookie cookie = new Cookie("JWT", token);
		cookie.setMaxAge(120 * 60 * 60); 
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
		
		return new ResponseEntity<String>(token, HttpStatus.OK);
	}
	
}
