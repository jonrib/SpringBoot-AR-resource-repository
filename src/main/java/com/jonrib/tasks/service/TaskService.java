package com.jonrib.tasks.service;

import java.util.List;
import java.util.Optional;

import com.jonrib.tasks.model.Task;
import com.jonrib.tasks.model.User;

public interface TaskService {
	List<Task> findAll();
	Optional<Task> findById(Long id);
	Task save(Task userGroup);
	void delete(Task userGroup);
	List<Task> findByUserIn(List<User> users);
}
