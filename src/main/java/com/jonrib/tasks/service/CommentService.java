package com.jonrib.tasks.service;

import java.util.List;
import java.util.Optional;

import com.jonrib.tasks.model.Comment;

public interface CommentService {
	Comment save(Comment entity);
	void delete(Comment entity);
	List<Comment> findAll();
	Optional<Comment> findById(Long id);
}
