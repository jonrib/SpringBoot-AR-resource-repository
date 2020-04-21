package tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.jonrib.tasks.WebApplication;
import com.jonrib.tasks.model.User;
import com.jonrib.tasks.repository.UserRepository;
import com.jonrib.tasks.service.ResourceEntryServiceImpl;
import com.jonrib.tasks.service.UserService;
import com.jonrib.tasks.service.UserServiceImpl;

public class UserServiceTests {
	private final UserRepository userRepository = Mockito.mock(UserRepository.class);
	private UserService userService;
	
	@BeforeEach
	void initUseCase(){
		userService = new UserServiceImpl(userRepository, new BCryptPasswordEncoder());
	}
	
	@Test
	void findById() {
		User user = new User();
		user.setId(Long.parseLong("0"));
		
		Mockito.when(userRepository.findById(Long.parseLong("0"))).then(new Answer<Optional<User>>() {
			@Override
			public Optional<User> answer(InvocationOnMock invocation) throws Throwable {
				return Optional.of(user);
			}
		});
		Mockito.when(userRepository.findById(Long.parseLong("1"))).then(new Answer<Optional<User>>() {
			@Override
			public Optional<User> answer(InvocationOnMock invocation) throws Throwable {
				return Optional.empty();
			}
		});
		
		assertEquals(user, userService.findById(Long.parseLong("0")));
		assertEquals(null, userService.findById(Long.parseLong("1")));
	}
	
	@Test
	void findAll() {
		User user = new User();
		user.setId(Long.parseLong("0"));
		
		Mockito.when(userRepository.findAll()).then(new Answer<List<User>>() {
			@Override
			public List<User> answer(InvocationOnMock invocation) throws Throwable {
				List<User> lst = new ArrayList<User>();
				lst.add(user);
				return lst;
			}
		});
		
		assertEquals(user, userService.findAll().get(0));
	}
	
	@Test
	void delete() {
		User user = new User();
		user.setId(Long.parseLong("0"));
		
		userService.delete(user);
	}
	
	@Test
	void save() {
		User user = new User();
		user.setId(Long.parseLong("0"));
		user.setPassword("test");
		
		Mockito.when(userRepository.save(user)).then(new Answer<User>() {
			@Override
			public User answer(InvocationOnMock invocation) throws Throwable {
				return user;
			}
		});
		
		userService.save(user);
		userService.update(user);
	}
	
	
}
