package com.jonrib.tasks.service;

import java.util.List;

import com.jonrib.tasks.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
    
    List<User> findAll ();
    
    User findById(Long id);
    void delete(User user);
    void update(User user);
}
