package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.jonrib.tasks.model.Comment;
import com.jonrib.tasks.model.Role;
import com.jonrib.tasks.repository.RoleRepository;
import com.jonrib.tasks.service.RoleService;
import com.jonrib.tasks.service.RoleServiceImpl;

public class RoleServiceTests {
	private final RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
	
	private RoleService roleService;
	
	@BeforeEach
	void initUseCase() {
		roleService = new RoleServiceImpl(roleRepository);
	}
	
	@Test
	void findById() {
		Role role = new Role();
		role.setId(Long.parseLong("1"));
		role.setName("");
		
		Mockito.when(roleRepository.findById(Long.parseLong("1"))).then(new Answer<Optional<Role>>() {
			@Override
			public Optional<Role> answer(InvocationOnMock invocation) throws Throwable {
				return Optional.of(role);
			}
		});
		Mockito.when(roleRepository.findById(Long.parseLong("0"))).then(new Answer<Optional<Role>>() {
			@Override
			public Optional<Role> answer(InvocationOnMock invocation) throws Throwable {
				return Optional.empty();
			}
		});
		assertEquals(role, roleService.findById((Long.parseLong("1"))));
		assertTrue(roleService.findById((Long.parseLong("0"))) == null);
	}
	
	@Test
	void findAll() {
		Role role = new Role();
		role.setId(Long.parseLong("1"));
		role.setName("");
		
		Mockito.when(roleRepository.findAll()).then(new Answer<List<Role>>() {
			@Override
			public List<Role> answer(InvocationOnMock invocation) throws Throwable {
				List<Role> list = new ArrayList<Role>();
				list.add(role);
				return list;
			}
		});
		Role roleS = roleService.findAll().get(0);
		assertEquals(role, roleS);
	}
	
	@Test
	void save() {
		Role role = new Role();
		role.setId(Long.parseLong("1"));
		role.setName("");
		
		Mockito.when(roleRepository.save(role)).then(new Answer<Role>() {
			@Override
			public Role answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return (Role) args[0];
			}
		});
		roleService.save(role);
	}
	
	@Test
	void delete() {
		Role role = new Role();
		role.setId(Long.parseLong("1"));
		role.setName("");
		roleService.delete(role);
	}
	
	@Test
	void findByName() {
		Role role = new Role();
		role.setId(Long.parseLong("1"));
		role.setName("test");
		
		Mockito.when(roleRepository.findByName("test")).then(new Answer<List<Role>>() {
			@Override
			public List<Role> answer(InvocationOnMock invocation) throws Throwable {
				List<Role> list = new ArrayList<Role>();
				list.add(role);
				return list;
			}
		});
		assertEquals(role, roleService.findByName("test"));
		Mockito.when(roleRepository.findByName("test2")).then(new Answer<List<Role>>() {
			@Override
			public List<Role> answer(InvocationOnMock invocation) throws Throwable {
				return new ArrayList<Role>();
			}
		});
		assertEquals(null, roleService.findByName("test2"));
	}
	
}
