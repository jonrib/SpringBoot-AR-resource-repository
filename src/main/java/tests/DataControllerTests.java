package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashSet;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonrib.tasks.WebApplication;
import com.jonrib.tasks.model.Role;
import com.jonrib.tasks.model.User;
import com.jonrib.tasks.repository.UserRepository;
import com.jonrib.tasks.service.CommentServiceImpl;
import com.jonrib.tasks.service.UserService;
import com.jonrib.tasks.web.DataController;

@SpringBootTest(classes=WebApplication.class)
@AutoConfigureMockMvc
public class DataControllerTests {
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Test
	void deleteJwt() throws Exception {
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/deleteJWT")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
	}
	@Test
	void getUser() throws Exception {		
		User testUser = new User();
		testUser.setUsername("test");
		testUser.setEmail("");
		testUser.setPassword("");
		testUser.setPasswordConfirm("");
		testUser.setRoles(new HashSet<Role>());
		userRepository.save(testUser);
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/user/test")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/user/test3")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		userRepository.delete(userRepository.findByUsername("test"));
	}
	
	
	@Test
	void putUser() throws Exception {
		User testUser = new User();
		testUser.setUsername("test2");
		testUser.setEmail("");
		testUser.setPassword("test");
		testUser.setPasswordConfirm("");
		testUser.setRoles(new HashSet<Role>());
		userService.save(testUser);
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/user/test2")
	            .contentType("application/json")
	            .content("{\"username\": \"test2\", \"password\": \"test234\", \"passwordConfirm\": \"test\", \"email\": \"test@test.test\"}"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/user/test3")
	            .contentType("application/json")
	            .content("{\"username\": \"test2\", \"password\": \"test234\", \"passwordConfirm\": \"test\", \"email\": \"test@test.test\"}"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/user/test2")
	            .contentType("application/json")
	            .content("{\"username\": \"test2\", \"password\": \"test234\", \"passwordConfirm\": \"testqqqq\", \"email\": \"test@test.test\"}"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(400));
		
		userRepository.delete(userRepository.findByUsername("test2"));
	}
}
