package com.jonrib.tasks.web;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jonrib.tasks.jwt.JwtTokenUtil;
import com.jonrib.tasks.model.User;
import com.jonrib.tasks.service.SecurityService;
import com.jonrib.tasks.service.UserService;
import com.jonrib.tasks.validator.UserValidator;


@Controller
public class NavigationController {
	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@GetMapping("/ui/registration")
	public String registration(Model model) {
		model.addAttribute("userForm", new User());

		return "registration";
	}

	@GetMapping("/login")
	public String login(Model model, String error, String logout, HttpServletResponse response) {
		if (error != null)
			model.addAttribute("error", "Your username and password is invalid.");

		if (logout != null) {
			model.addAttribute("message", "You have been logged out successfully.");
			Cookie cookie = new Cookie("JWT", null);
			cookie.setMaxAge(0);
			//cookie.setHttpOnly(true);
			response.addCookie(cookie);
		}
		
		
		return "login";
	}


	@GetMapping({"/"})
	public void main(Model model, HttpServletResponse response) throws IOException {
		if (securityService.findLoggedInUsername() != "" && securityService.findLoggedInUsername() != null) {
			
		}
		response.sendRedirect("/ui/welcome");
	}

	@GetMapping({"/ui/welcome"})
	public String welcome(Model model) {
		return "welcome";
	}
	@GetMapping({"/ui/tasks"})
	public String tasks(Model model) {
		return "tasks";
	}
	@GetMapping({"/ui/groups"})
	public String groups(Model model) {
		return "groupslist";
	}
	@GetMapping({"/ui/groupform"})
	public String createGroup(Model model) {
		return "groupform";
	}
	@GetMapping({"/ui/groupform/*"})
	public String editGroup(Model model) {
		return "groupform";
	}

	@GetMapping({"/ui/users"})
	public String users(Model model) {
		return "userslist";
	}
	
	@GetMapping({"/ui/userform/*"})
	public String editUser(Model model) {
		return "userform";
	}
	
	@GetMapping({"/ui/projects"})
	public String projects(Model model) {
		return "projectslist";
	}
	@GetMapping({"/ui/projectform"})
	public String createProject(Model model) {
		return "projectform";
	}
	@GetMapping({"/ui/projectform/*"})
	public String editProject(Model model) {
		return "projectform";
	}

	@PostMapping("/ui/registration")
	public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, HttpServletResponse response) {
		userValidator.validate(userForm, bindingResult);

		if (bindingResult.hasErrors()) {
			return "registration";
		}

		userService.save(userForm);

		final String token = securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
		//Cookie jwtCookie = new Cookie("JWT", token);
		//jwtCookie.setMaxAge(120*60*60);
		//response.addCookie(jwtCookie);

		return "redirect:/ui/welcome";
	}
	
	@PostMapping("/login")
	public String login(Model model, HttpServletResponse response, HttpServletRequest request) {
		String token = null;
		try {
			token = securityService.autoLogin(request.getParameter("username").toString(), request.getParameter("password").toString());
		}catch (Exception e) {
			model.addAttribute("error", "Your username and password is invalid.");
			return "login";
		}
		Cookie cookie = new Cookie("JWT", token);
		cookie.setMaxAge(120 * 60 * 60); 
		response.addCookie(cookie);
		
		return "redirect:/ui/welcome";
	}
	
}
