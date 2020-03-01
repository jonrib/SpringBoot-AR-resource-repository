package com.jonrib.tasks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonrib.tasks.model.Task;
import com.jonrib.tasks.model.User;

public interface TaskRepository extends JpaRepository<Task, Long>{
	List<Task> findByUsersIn(List<User> users);
}
