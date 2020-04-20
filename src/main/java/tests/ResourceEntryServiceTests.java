package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.jonrib.tasks.model.Comment;
import com.jonrib.tasks.model.ResourceEntry;
import com.jonrib.tasks.model.Role;
import com.jonrib.tasks.model.User;
import com.jonrib.tasks.repository.CommentRepository;
import com.jonrib.tasks.repository.ResourceEntryRepository;
import com.jonrib.tasks.service.CommentService;
import com.jonrib.tasks.service.CommentServiceImpl;
import com.jonrib.tasks.service.ResourceEntryService;
import com.jonrib.tasks.service.ResourceEntryServiceImpl;
import com.jonrib.tasks.service.SecurityService;
import com.jonrib.tasks.service.UserService;

public class ResourceEntryServiceTests {
	private final ResourceEntryRepository resourceEntryRepository = Mockito.mock(ResourceEntryRepository.class);
	private final SecurityService securityService = Mockito.mock(SecurityService.class);
	private final UserService userService = Mockito.mock(UserService.class);
	
	private ResourceEntryService resourceEntryService;
	
	@BeforeEach
	void initUseCase() {
		resourceEntryService = new ResourceEntryServiceImpl(resourceEntryRepository, securityService, userService);
	}
	
	@Test
	void save() {
		ResourceEntry resourceEntry = new ResourceEntry();
		resourceEntry.setAuthor(null);
		resourceEntry.setCategory("");
		resourceEntry.setComments(null);
		resourceEntry.setDescription("");
		resourceEntry.setEditors(null);
		resourceEntry.setFiles(null);
		resourceEntry.setHistories(null);
		resourceEntry.setId(Long.parseLong("1"));
		resourceEntry.setImages(null);
		resourceEntry.setPrivate(false);
		resourceEntry.setReaders(null);
		resourceEntry.setTags(null);
		resourceEntry.setTitle("");
		Mockito.when(resourceEntryRepository.save(resourceEntry)).then(new Answer<ResourceEntry>() {
			@Override
			public ResourceEntry answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return (ResourceEntry) args[0];
			}
		});
		ResourceEntry resourceEntrySaved = (ResourceEntry) resourceEntryService.save(resourceEntry);
		assertEquals(resourceEntry, resourceEntrySaved);
	}
	
	@Test
	void delete() {
		ResourceEntry resourceEntry = new ResourceEntry();
		resourceEntryService.delete(resourceEntry);
	}
	
	@Test
	void findAll() {
		ResourceEntry resourceEntry = new ResourceEntry();
		resourceEntry.setAuthor(null);
		resourceEntry.setCategory("");
		resourceEntry.setComments(null);
		resourceEntry.setDescription("");
		resourceEntry.setEditors(null);
		resourceEntry.setFiles(null);
		resourceEntry.setHistories(null);
		resourceEntry.setId(Long.parseLong("1"));
		resourceEntry.setImages(null);
		resourceEntry.setPrivate(false);
		resourceEntry.setReaders(null);
		resourceEntry.setTags(null);
		resourceEntry.setTitle("");
		Mockito.when(resourceEntryRepository.findAll()).then(new Answer<List<ResourceEntry>>() {
			@Override
			public List<ResourceEntry> answer(InvocationOnMock invocation) throws Throwable {
				List<ResourceEntry> list = new ArrayList<ResourceEntry>();
				list.add(resourceEntry);
				return list;
			}
		});
		ResourceEntry resourceEntryS = resourceEntryService.findAll().get(0);
		assertEquals(resourceEntryS, resourceEntry);
	}
	
	@Test
	void findById() {
		ResourceEntry resourceEntry = new ResourceEntry();
		resourceEntry.setAuthor(null);
		resourceEntry.setCategory("");
		resourceEntry.setComments(null);
		resourceEntry.setDescription("");
		resourceEntry.setEditors(null);
		resourceEntry.setFiles(null);
		resourceEntry.setHistories(null);
		resourceEntry.setId(Long.parseLong("1"));
		resourceEntry.setImages(null);
		resourceEntry.setPrivate(false);
		resourceEntry.setReaders(null);
		resourceEntry.setTags(null);
		resourceEntry.setTitle("");
		Mockito.when(resourceEntryRepository.findById(Long.parseLong("1"))).then(new Answer<Optional<ResourceEntry>>() {
			@Override
			public Optional<ResourceEntry> answer(InvocationOnMock invocation) throws Throwable {
				return Optional.of(resourceEntry);
			}
		});
		ResourceEntry resourceEntryS = resourceEntryService.findById(Long.parseLong("1")).get();
		assertEquals(resourceEntryS, resourceEntry);
	}
	
	@Test
	void canReadEdit() {	
		Role adminR = new Role();
		adminR.setName("Admin");
		Set<Role> adminRole = new HashSet<Role>();
		adminRole.add(adminR);
		
		User author = new User();
		author.setUsername("test1");
		User editor = new User();
		editor.setRoles(new HashSet<Role>());
		editor.setUsername("test2");
		User reader = new User();
		reader.setUsername("test3");
		User admin = new User();
		admin.setUsername("admin");
		admin.setRoles(adminRole);
		
		Mockito.when(userService.findByUsername("test1")).then(new Answer<User>() {
			@Override
			public User answer(InvocationOnMock invocation) {
				return author;
			}
		});
		Mockito.when(userService.findByUsername("test2")).then(new Answer<User>() {
			@Override
			public User answer(InvocationOnMock invocation) {
				return editor;
			}
		});
		Mockito.when(userService.findByUsername("test3")).then(new Answer<User>() {
			@Override
			public User answer(InvocationOnMock invocation) {
				return reader;
			}
		});
		Mockito.when(userService.findByUsername("admin")).then(new Answer<User>() {
			@Override
			public User answer(InvocationOnMock invocation) {
				return admin;
			}
		});
		
		Mockito.when(securityService.findLoggedInUsername("test1")).then(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation) {
				return "test1";
			}
		});
		Mockito.when(securityService.findLoggedInUsername("test2")).then(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation) {
				return "test2";
			}
		});
		Mockito.when(securityService.findLoggedInUsername("test3")).then(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation) {
				return "test3";
			}
		});
		Mockito.when(securityService.findLoggedInUsername("admin")).then(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation) {
				return "admin";
			}
		});
		
		Mockito.when(securityService.findLoggedInUsername("")).then(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation) {
				return "anonymousUser";
			}
		});
		
		Mockito.when(userService.findByUsername("anonymousUser")).then(new Answer<User>() {
			@Override
			public User answer(InvocationOnMock invocation) {
				return null;
			}
		});
		
		
		Set<User> list1 = new HashSet<User>();
		Set<User> list2 = new HashSet<User>();
		Set<User> list3 = new HashSet<User>();
		
		list1.add(author);
		list2.add(editor);
		list3.add(reader);
		
		ResourceEntry resourceEntry = new ResourceEntry();
		resourceEntry.setAuthor(list1);
		resourceEntry.setCategory("");
		resourceEntry.setComments(null);
		resourceEntry.setDescription("");
		resourceEntry.setEditors(list2);
		resourceEntry.setFiles(null);
		resourceEntry.setHistories(null);
		resourceEntry.setId(Long.parseLong("1"));
		resourceEntry.setImages(null);
		resourceEntry.setPrivate(false);
		resourceEntry.setReaders(list3);
		resourceEntry.setTags(null);
		resourceEntry.setTitle("");
		
		assertTrue(resourceEntryService.canEdit(resourceEntry, "test1"));
		assertTrue(resourceEntryService.canEdit(resourceEntry, "test2"));
		assertTrue(!resourceEntryService.canEdit(resourceEntry, "test3"));
		assertTrue(resourceEntryService.canEdit(resourceEntry, "admin"));
		assertTrue(resourceEntryService.canRead(resourceEntry, ""));
		resourceEntry.setPrivate(true);
		assertTrue(resourceEntryService.canEdit(resourceEntry, "test1"));
		assertTrue(resourceEntryService.canEdit(resourceEntry, "test2"));
		assertTrue(!resourceEntryService.canEdit(resourceEntry, "test3"));
		assertTrue(!resourceEntryService.canRead(resourceEntry, ""));
		assertTrue(resourceEntryService.canRead(resourceEntry, "test3"));
		assertTrue(!resourceEntryService.canEdit(resourceEntry, ""));
	}
	
	@Test
	void findByCategoryAndTags() {
		ResourceEntry resourceEntry = new ResourceEntry();
		resourceEntry.setCategory("test");
		resourceEntry.setComments(null);
		resourceEntry.setDescription("");
		resourceEntry.setFiles(null);
		resourceEntry.setHistories(null);
		resourceEntry.setId(Long.parseLong("1"));
		resourceEntry.setImages(null);
		resourceEntry.setPrivate(false);
		resourceEntry.setTags(null);
		resourceEntry.setTitle("");
		
		Mockito.when(resourceEntryRepository.findByCategory("test")).then(new Answer<List<ResourceEntry>>() {
			@Override
			public List<ResourceEntry> answer(InvocationOnMock invocation) {
				List<ResourceEntry> ans = new ArrayList<ResourceEntry>();
				ans.add(resourceEntry);
				return ans;
			}
		});
		
		assertEquals(resourceEntry, resourceEntryService.findByCategory("test").get(0));
		
		List<String> lst = new ArrayList<String>();
		lst.add("a");
		resourceEntry.setTags(new HashSet<String>(lst));
		
		Mockito.when(resourceEntryRepository.findByTagsIn(lst)).then(new Answer<List<ResourceEntry>>() {
			@Override
			public List<ResourceEntry> answer(InvocationOnMock invocation) {
				List<ResourceEntry> ans = new ArrayList<ResourceEntry>();
				ans.add(resourceEntry);
				return ans;
			}
		});
		
		assertEquals(resourceEntry, resourceEntryService.findByTagsIn(lst).get(0));
	}
	
	/*
	boolean canRead(ResourceEntry entity, String token);
	boolean canEdit(ResourceEntry entity, String token);
	List<ResourceEntry> findByCategory(String category);
	List<ResourceEntry> findByTagsIn(List<String> tags);
	 */
}
