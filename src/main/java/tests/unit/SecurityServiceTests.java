package tests.unit;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.jonrib.tasks.WebApplication;
import com.jonrib.tasks.jwt.JwtTokenUtil;
import com.jonrib.tasks.model.Role;
import com.jonrib.tasks.model.User;
import com.jonrib.tasks.repository.UserRepository;
import com.jonrib.tasks.service.SecurityServiceImpl;
import com.jonrib.tasks.service.UserDetailsServiceImpl;
import com.jonrib.tasks.service.UserServiceImpl;

public class SecurityServiceTests {
	SecurityServiceImpl securityServiceImpl;
	private final UserRepository userRepository = Mockito.mock(UserRepository.class);
	
	@Autowired
    private AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);


	@BeforeEach
	void initUseCase() {
		securityServiceImpl = new SecurityServiceImpl(new UserDetailsServiceImpl(userRepository), authenticationManager, new JwtTokenUtil(new UserServiceImpl(userRepository)));
	}


	@Test
	void findLoggedInUsernameTestNull() {
		String loggedIn = securityServiceImpl.findLoggedInUsername("");
		assertEquals("anonymousUser", loggedIn);
	}
	/*
	@Test
	void findLoggedInUsernameTest() {
		UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);
		Mockito.when(userRepository.findByUsername("jonrib")).then(new Answer<User>() {@Override
			public User answer(InvocationOnMock invocation) throws Throwable {
				User user = new User();
				user.setUsername("jonrib");
				user.setPassword(new BCryptPasswordEncoder().encode("test"));
				Set<Role> roles = new HashSet<Role>();
				Role role = new Role();
				role.setName("test");
				roles.add(role);
				user.setRoles(roles);
				return user;
			}
		});
		String loggedIn = securityServiceImpl.findLoggedInUsername("Bearer eyJhbGciOiJIUzUxMiJ9.eyJSb2xlIjpbXSwic3ViIjoiam9ucmliIiwiZXhwIjoxNTg3NDEzNjU1LCJpYXQiOjE1ODczOTU2NTV9.mrorzHNSFjXhc9htiaNoKNomTQSfIRaig2M-HTFFXcKOfpFz2M6_NvM3HHrb8KxSS6peTPjW0oJKKKRRL8ir_Q");
		assertEquals("jonrib", loggedIn);
		
		UserDetails userDetails = userDetailsService.loadUserByUsername("jonrib");
	}
	*/
}
