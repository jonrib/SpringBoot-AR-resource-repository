package tests;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonrib.tasks.WebApplication;
import com.jonrib.tasks.model.Role;
import com.jonrib.tasks.model.User;
import com.jonrib.tasks.service.RoleService;
import com.jonrib.tasks.service.UserService;

@SpringBootTest(classes=WebApplication.class)
@AutoConfigureMockMvc
public class RoleControllerTests {
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;

	@Test
	void testRoles() throws Exception {		
		Role role = new Role();
		role.setName("Admin");
		roleService.save(role);
		
		Role role2 = new Role();
		role2.setName("Admin2");
		
		User admin = new User();
		admin.setEmail("");
		admin.setUsername("adminTEST");
		admin.setPassword("adminTEST10123!@#$$$S@");
		Set<Role> roles = new HashSet<Role>();
		roles.add(roleService.findByName("Admin"));
		admin.setRoles(roles);
		userService.save(admin);
		
		User notAdmin = new User();
		notAdmin.setEmail("");
		notAdmin.setUsername("test");
		notAdmin.setPassword("test");
		userService.save(notAdmin);
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/roles")
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(role)))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/roles")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/roles/"+roleService.findByName("Admin").getId())
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/roles/"+roleService.findByName("Admin").getId())
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		String token = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/login")
	            .contentType("application/json")
	            .param("username", "test")
	            .param("password", "test"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		Cookie JWT = new Cookie("JWT", token);
		JWT.setHttpOnly(true);

		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/roles")
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(role))
	            .cookie(JWT))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		token = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/login")
	            .contentType("application/json")
	            .param("username", "adminTEST")
	            .param("password", "adminTEST10123!@#$$$S@"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		JWT = new Cookie("JWT", token);
		JWT.setHttpOnly(true);
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/roles")
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(role2))
	            .cookie(JWT))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/roles")
	            .contentType("application/json")
	            .cookie(JWT))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/roles/"+roleService.findByName("Admin2").getId())
	            .contentType("application/json")
	            .cookie(JWT))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/roles")
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(role2))
	            .cookie(JWT))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(400));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/roles/"+roleService.findByName("Admin2").getId())
	            .contentType("application/json")
	            .cookie(JWT))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/roles/"+666666666)
	            .contentType("application/json")
	            .cookie(JWT))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/roles/"+565416451)
	            .contentType("application/json")
	            .cookie(JWT))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		roleService.delete(roleService.findByName("Admin"));
		userService.delete(userService.findByUsername("adminTEST"));
		userService.delete(userService.findByUsername("test"));
	}
}
