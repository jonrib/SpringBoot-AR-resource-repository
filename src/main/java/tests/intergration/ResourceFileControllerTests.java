package tests.intergration;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

import ch.qos.logback.core.status.Status;

@SpringBootTest(classes=WebApplication.class)
@AutoConfigureMockMvc
public class ResourceFileControllerTests {
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
	void testResourceFiles() throws Exception {
		Role role = new Role();
		role.setName("Admin");
		roleService.save(role);
		
		User admin = new User();
		admin.setEmail("");
		admin.setUsername("adminTEST5");
		admin.setPassword("adminTEST10123!@#$$$S@");
		Set<Role> roles = new HashSet<Role>();
		roles.add(roleService.findByName("Admin"));
		admin.setRoles(roles);
		userService.save(admin);
		
		User notAdmin = new User();
		notAdmin.setEmail("");
		notAdmin.setUsername("test5");
		notAdmin.setPassword("test");
		userService.save(notAdmin);
		
		ResourceEntry simpleTest = new ResourceEntry();
		simpleTest.setAuthor(new HashSet<User>());
		simpleTest.setEditors(new HashSet<User>());
		simpleTest.setReaders(new HashSet<User>());
		simpleTest.getAuthor().add(admin);
		simpleTest = resourceEntryService.save(simpleTest);

		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/"+5165156+"/files")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/"+simpleTest.getId()+"/files")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		simpleTest.setPrivate(true);
		simpleTest = resourceEntryService.save(simpleTest);
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/"+simpleTest.getId()+"/files")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/"+4532452+"/files/"+51651)
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/"+simpleTest.getId()+"/files/"+51651)
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		simpleTest.setPrivate(false);
		simpleTest = resourceEntryService.save(simpleTest);
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/"+simpleTest.getId()+"/files/"+51651)
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "text/plain", "sdfsaerfawerdawsd".getBytes());
		MockMultipartFile secondFile = new MockMultipartFile("file", "filename.png", "text/plain", "sdfsaerfawerdawsd".getBytes());
		MockMultipartFile thirdFile = new MockMultipartFile("file", "filename.png", "text/plain", "sdfsaerfawerdawsd".getBytes());
		
		mockMvc.perform(MockMvcRequestBuilders.multipart("/resourceEntries/"+simpleTest.getId()+"/files").file(firstFile)).andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		String token = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/login")
	            .contentType("application/json")
	            .param("username", "adminTEST5")
	            .param("password", "adminTEST10123!@#$$$S@"))
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		Cookie JWT = new Cookie("JWT", token);
		JWT.setHttpOnly(true);
		
		mockMvc.perform(MockMvcRequestBuilders.multipart("/resourceEntries/"+simpleTest.getId()+"/files").file(firstFile).file(secondFile).file(thirdFile).cookie(JWT)).andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resourceEntries/"+simpleTest.getId()+"/files/"+resourceEntryService.findById(simpleTest.getId()).get().getFiles().stream().findFirst().get().getId())
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/resourceEntries/"+simpleTest.getId()+"/files/"+resourceEntryService.findById(simpleTest.getId()).get().getFiles().stream().findFirst().get().getId())
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/resourceEntries/"+4523453+"/files/"+resourceEntryService.findById(simpleTest.getId()).get().getFiles().stream().findFirst().get().getId())
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/resourceEntries/"+simpleTest.getId()+"/files/"+15416516)
	            .contentType("application/json")
	            .cookie(JWT)
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/resourceEntries/"+simpleTest.getId()+"/files/"+resourceEntryService.findById(simpleTest.getId()).get().getFiles().stream().findFirst().get().getId())
	            .contentType("application/json")
	            .cookie(JWT)
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/resourceEntries/"+simpleTest.getId()+"/files")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(403));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/resourceEntries/"+4523453+"/files")
	            .contentType("application/json")
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(404));
		
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/resourceEntries/"+simpleTest.getId()+"/files")
	            .contentType("application/json")
	            .cookie(JWT)
	            )
	            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
		
		
		roleService.delete(roleService.findByName("Admin"));
		userService.delete(userService.findByUsername("adminTEST5"));
		userService.delete(userService.findByUsername("test5"));
		resourceEntryService.delete(simpleTest);
		
	}
	
}
