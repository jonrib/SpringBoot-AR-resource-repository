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
import com.jonrib.tasks.WebApplication;
import com.jonrib.tasks.model.ResourceEntry;
import com.jonrib.tasks.model.Role;
import com.jonrib.tasks.model.User;
import com.jonrib.tasks.repository.HistoryRepository;
import com.jonrib.tasks.service.ResourceEntryService;
import com.jonrib.tasks.service.RoleService;
import com.jonrib.tasks.service.SecurityService;
import com.jonrib.tasks.service.StorageService;
import com.jonrib.tasks.service.UserService;

@SpringBootTest(classes=WebApplication.class)
@AutoConfigureMockMvc
public class ResourceEntryControllerTests {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ResourceEntryService resourceEntryService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Test
	void testResourceEntries() throws Exception {		
		Role role = new Role();
		role.setName("Admin");
		roleService.save(role);
		
		User admin = new User();
		admin.setEmail("");
		admin.setUsername("adminTEST2");
		admin.setPassword("adminTEST10123!@#$$$S@");
		Set<Role> roles = new HashSet<Role>();
		roles.add(roleService.findByName("Admin"));
		admin.setRoles(roles);
		userService.save(admin);
		
		User notAdmin = new User();
		notAdmin.setEmail("");
		notAdmin.setUsername("test2");
		notAdmin.setPassword("test");
		userService.save(notAdmin);
		
		ResourceEntry simpleTest = new ResourceEntry();
		simpleTest.setCategory("test");
		simpleTest.setAuthor(new HashSet<User>());
		simpleTest.setEditors(new HashSet<User>());
		simpleTest.setReaders(new HashSet<User>());
		simpleTest.getAuthor().add(admin);
		simpleTest = resourceEntryService.save(simpleTest);
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		String returnJson = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/category/test")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		
		ResourceEntry[] test = mapper.readValue(returnJson, ResourceEntry[].class);
		assertEquals(test[0].getId(), simpleTest.getId());
		
		returnJson = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/category/test2")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		
		test = mapper.readValue(returnJson, ResourceEntry[].class);
		assertEquals(test.length, 0);
		
		simpleTest.setTags(new HashSet<String>());
		simpleTest.getTags().add("test1");
		simpleTest.getTags().add("test2");
		simpleTest = resourceEntryService.save(simpleTest);
		
		returnJson = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/tags/test2")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		test = mapper.readValue(returnJson, ResourceEntry[].class);
		assertEquals(test[0].getId(), simpleTest.getId());
		
		returnJson = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/tags/test1")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		test = mapper.readValue(returnJson, ResourceEntry[].class);
		assertEquals(test[0].getId(), simpleTest.getId());
		
		returnJson = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/tags/test1 test2")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		test = mapper.readValue(returnJson, ResourceEntry[].class);
		assertEquals(test[0].getId(), simpleTest.getId());
		
		returnJson = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/tags/test3")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		test = mapper.readValue(returnJson, ResourceEntry[].class);
		assertEquals(test.length, 0);
		
		returnJson = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/"+simpleTest.getId())
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		
		assertEquals(mapper.readValue(returnJson, ResourceEntry.class).getId(), simpleTest.getId());
		
		simpleTest.setPrivate(true);
		resourceEntryService.save(simpleTest);
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/"+simpleTest.getId())
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/"+51658165)
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		ResourceEntry entryToPost = new ResourceEntry();
		entryToPost.setTitle("TEST FROM TESTING");
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/resourceEntries")
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(entryToPost)))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/resourceEntries/"+simpleTest.getId())
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/resourceEntries/"+541515)
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/resourceEntries/"+541515)
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(entryToPost)))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		String token = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/login")
	            .contentType("application/json")
	            .param("username", "test2")
	            .param("password", "test"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		Cookie JWT = new Cookie("JWT", token);
		JWT.setHttpOnly(true);
		
		entryToPost = mapper.readValue(mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/resourceEntries")
	            .contentType("application/json")
	            .content(mapper.writeValueAsString(entryToPost))
	            .cookie(JWT))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString(), ResourceEntry.class);
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/resourceEntries/"+simpleTest.getId())
	            .contentType("application/json")
	            .cookie(JWT))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/resourceEntries/"+simpleTest.getId())
	            .contentType("application/json")
	            .cookie(JWT)
	            .content(mapper.writeValueAsString(entryToPost)))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		entryToPost = mapper.readValue(mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/resourceEntries/"+entryToPost.getId())
	            .contentType("application/json")
	            .cookie(JWT)
	            .content("{\"readers\":[\"test2\"], \"editors\": [\"test2\", \"random\"], \"entry\":"+mapper.writeValueAsString(entryToPost)+"}"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString(), ResourceEntry.class);

		assertEquals(entryToPost.getReaders().stream().anyMatch(x -> x.getUsername().equals("test2")), true);
		assertEquals(entryToPost.getEditors().stream().anyMatch(x -> x.getUsername().equals("test2")), true);
		assertEquals(entryToPost.getReaders().stream().anyMatch(x -> x.getUsername().equals("random")), false);
		assertEquals(entryToPost.getReaders().stream().anyMatch(x -> x.getUsername().equals("adminTEST2")), false);
		assertEquals(entryToPost.getEditors().stream().anyMatch(x -> x.getUsername().equals("adminTEST2")), false);
		

		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/resourceEntries/"+entryToPost.getId())
	            .contentType("application/json")
	            .cookie(JWT))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		roleService.delete(roleService.findByName("Admin"));
		userService.delete(userService.findByUsername("adminTEST2"));
		userService.delete(userService.findByUsername("test2"));
		resourceEntryService.delete(simpleTest);
	}

}
