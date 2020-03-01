package com.jonrib.tasks.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jonrib.tasks.model.Comment;
import com.jonrib.tasks.model.Task;
import com.jonrib.tasks.model.User;
import com.jonrib.tasks.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private CommentService commentService;
	
	@Override
	public List<Task> findAll() {
		return taskRepository.findAll();
	}

	@Override
	public Optional<Task> findById(Long id) {
		return taskRepository.findById(id);
	}

	@Override
	public Task save(Task entity) {
		return taskRepository.save(entity);
	}

	@Override
	public void delete(Task entity) {
		entity.setAuthor(null);
		entity.setUsersDone(null);
		for (Comment comment : entity.getComments()) {
			commentService.delete(comment);
		}
		taskRepository.delete(entity);		
	}

	@Override
	public List<Task> findByUserIn(List<User> users) {
		return taskRepository.findByUsersIn(users);
	}
	
}
