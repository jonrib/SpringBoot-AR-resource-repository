package tests.intergration;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonrib.app.WebApplication;
import com.jonrib.app.model.Role;
import com.jonrib.app.model.User;
import com.jonrib.app.service.RoleService;
import com.jonrib.app.service.UserService;

@SpringBootTest(classes=WebApplication.class)
@AutoConfigureMockMvc
public class UserControllerTests {
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;

	@Test
	void testUsers() throws Exception {		
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
		notAdmin.setUsername("test123");
		notAdmin.setPassword("test12asds3");
		notAdmin.setPasswordConfirm("test12asds3");
		//userService.save(notAdmin);
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/users")
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(notAdmin)))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/users")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/users/"+userService.findByUsername("test123").getId())
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/users/"+51651651)
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/users/"+userService.findByUsername("test123").getId())
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/users/"+userService.findByUsername("test123").getId())
	            .contentType("application/json")
	            .content("{ \"users\": {\"roles\": [\"Admin\"]}}"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		String token = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/login")
	            .contentType("application/json")
	            .param("username", "adminTEST")
	            .param("password", "adminTEST10123!@#$$$S@"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		Cookie JWT = new Cookie("JWT", token);
		JWT.setHttpOnly(true);

		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/users/"+userService.findByUsername("test123").getId())
	            .contentType("application/json")
	            .cookie(JWT))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/roles/"+565416451)
	            .contentType("application/json")
	            .cookie(JWT))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		notAdmin.setUsername("test");
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/users")
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(notAdmin)))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(400));
		notAdmin.setUsername("testasdasfasedfawdadsfvasdasdaasdasvasvasvsfasfasdf");
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/users")
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(notAdmin)))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(400));
		notAdmin.setUsername("testas123");
		notAdmin.setPassword("teasdasasdasd");
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/users")
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(notAdmin)))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(400));
		notAdmin.setPassword("test12asds3asdasfvawfawsvasvasvasvaasdasasvasvsvasd");
		notAdmin.setPasswordConfirm("test12asds3asdasfvawfawsvasvasvasvaasdasasvasvsvasd");
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/users")
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(notAdmin)))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(400));
		notAdmin.setPassword("asdasdasd");
		notAdmin.setPasswordConfirm("asdasdasd");
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/users")
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(notAdmin)))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/users/"+userService.findByUsername("testas123").getId())
	            .contentType("application/json")
	            .cookie(JWT)
	            .content("{ \"users\": {\"roles\": [\"Admin\"]}}"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/users/"+userService.findByUsername("testas123").getId())
	            .contentType("application/json")
	            .cookie(JWT)
	            .content("{ \"users\": {\"roles\": [\"Randommm\"]}}"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(400));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/users/"+56165156)
	            .contentType("application/json")
	            .cookie(JWT)
	            .content("{ \"users\": {\"roles\": [\"Admin\"]}}"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(400));
		
		
		roleService.delete(roleService.findByName("Admin"));
		userService.delete(userService.findByUsername("adminTEST"));
	}
}
