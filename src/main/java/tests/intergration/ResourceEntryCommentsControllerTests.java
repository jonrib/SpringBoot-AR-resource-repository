package tests.intergration;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import com.jonrib.app.model.Comment;
import com.jonrib.app.model.ResourceEntry;
import com.jonrib.app.model.Role;
import com.jonrib.app.model.User;
import com.jonrib.app.repository.HistoryRepository;
import com.jonrib.app.service.ResourceEntryService;
import com.jonrib.app.service.RoleService;
import com.jonrib.app.service.SecurityService;
import com.jonrib.app.service.StorageService;
import com.jonrib.app.service.UserService;

@SpringBootTest(classes=WebApplication.class)
@AutoConfigureMockMvc
public class ResourceEntryCommentsControllerTests {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ResourceEntryService resourceEntryService;
	@Autowired
	private StorageService storageService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private UserService userService;
	@Autowired
	private HistoryRepository historyRepository;
	@Autowired
	private RoleService roleService;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Test
	void testResourceComments() throws Exception {		
		Role role = new Role();
		role.setName("Admin");
		roleService.save(role);
		
		User admin = new User();
		admin.setEmail("");
		admin.setUsername("adminTEST3");
		admin.setPassword("adminTEST10123!@#$$$S@");
		Set<Role> roles = new HashSet<Role>();
		roles.add(roleService.findByName("Admin"));
		admin.setRoles(roles);
		userService.save(admin);
		
		User notAdmin = new User();
		notAdmin.setEmail("");
		notAdmin.setUsername("test3");
		notAdmin.setPassword("test");
		userService.save(notAdmin);
		
		ResourceEntry simpleTest = new ResourceEntry();
		simpleTest.setAuthor(new HashSet<User>());
		simpleTest.setEditors(new HashSet<User>());
		simpleTest.setReaders(new HashSet<User>());
		simpleTest.getAuthor().add(admin);
		simpleTest = resourceEntryService.save(simpleTest);
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/"+2515651+"/comments")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/"+simpleTest.getId()+"/comments")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		simpleTest.setPrivate(true);
		simpleTest = resourceEntryService.save(simpleTest);
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/"+simpleTest.getId()+"/comments")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		Comment comm = new Comment();
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/resourceEntries/"+453483+"/comments")
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(comm)))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		simpleTest.setPrivate(false);
		simpleTest = resourceEntryService.save(simpleTest);
		
		comm = mapper.readValue(mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/resourceEntries/"+simpleTest.getId()+"/comments")
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(comm)))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString(), Comment.class);
		simpleTest.setPrivate(true);
		simpleTest = resourceEntryService.save(simpleTest);
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/resourceEntries/"+simpleTest.getId()+"/comments")
		            .contentType("application/json")
		            .content(mapper.writeValueAsString(comm)))
		            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/resourceEntries/"+453483+"/comments/1")
	            .contentType("application/json"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		simpleTest.setPrivate(true);
		resourceEntryService.save(simpleTest);
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/resourceEntries/"+simpleTest.getId()+"/comments/"+comm.getId())
	            .contentType("application/json"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		Comment reply = new Comment();
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/resourceEntries/"+simpleTest.getId()+"/comments/"+comm.getId())
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(reply)))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/resourceEntries/"+541564165+"/comments/"+comm.getId())
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(reply)))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/resourceEntries/"+simpleTest.getId()+"/comments/"+45345312)
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(reply)))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		simpleTest.setPrivate(false);
		resourceEntryService.save(simpleTest);
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/resourceEntries/"+simpleTest.getId()+"/comments/"+comm.getId())
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(reply)))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		String token = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/login")
	            .contentType("application/json")
	            .param("username", "adminTEST3")
	            .param("password", "adminTEST10123!@#$$$S@"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		Cookie JWT = new Cookie("JWT", token);
		JWT.setHttpOnly(true);
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/resourceEntries/"+simpleTest.getId()+"/comments/"+comm.getId())
	            .contentType("application/json")
	            .cookie(JWT))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/resourceEntries/"+simpleTest.getId()+"/comments/"+156165)
	            .contentType("application/json")
	            .cookie(JWT))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		roleService.delete(roleService.findByName("Admin"));
		userService.delete(userService.findByUsername("adminTEST3"));
		userService.delete(userService.findByUsername("test3"));
		resourceEntryService.delete(simpleTest);
	}
}
