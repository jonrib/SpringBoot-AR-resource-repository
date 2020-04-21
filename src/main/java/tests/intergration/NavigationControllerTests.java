package tests.intergration;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonrib.app.WebApplication;
import com.jonrib.app.model.Role;
import com.jonrib.app.model.User;
import com.jonrib.app.repository.UserRepository;
import com.jonrib.app.service.UserService;

@SpringBootTest(classes=WebApplication.class)
@AutoConfigureMockMvc
public class NavigationControllerTests {
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;

	@Test
	void login() throws Exception {		
		User testUser = new User();
		testUser.setUsername("test");
		testUser.setEmail("");
		testUser.setPassword("test");
		testUser.setPasswordConfirm("");
		testUser.setRoles(new HashSet<Role>());
		userService.save(testUser);
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/login")
	            .contentType("application/json")
	            .param("username", "test")
	            .param("password", "test"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/login")
	            .contentType("application/json")
	            .param("username", "testas")
	            .param("password", "test"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(400));
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/login")
	            .contentType("application/json")
	            .param("username", "testas")
	            .param("password", "testas"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(400));
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/login")
	            .contentType("application/json")
	            .param("username", "test")
	            .param("password", "testas"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(400));
		
		userRepository.delete(userRepository.findByUsername("test"));
	}

}
