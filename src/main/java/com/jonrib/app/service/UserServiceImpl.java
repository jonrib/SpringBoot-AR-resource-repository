package com.jonrib.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jonrib.app.model.User;
import com.jonrib.app.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository2) {
		this.userRepository = userRepository2;
	}
    
    public UserServiceImpl(UserRepository userRepository2, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository2;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
    
    public UserServiceImpl() { }

	@Override
    public void update(User user) {
    	userRepository.save(user);
    }
    
    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User findById(Long id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent())
			return user.get();
		else
			return null;
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}
}
