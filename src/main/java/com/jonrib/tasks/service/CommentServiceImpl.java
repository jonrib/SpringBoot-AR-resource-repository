package com.jonrib.tasks.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jonrib.tasks.model.Comment;
import com.jonrib.tasks.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentRepository commentRepository;

	public CommentServiceImpl() { }
	
	public CommentServiceImpl(CommentRepository commentRepository2) {
		this.commentRepository = commentRepository2;
	}

	@Override
	public Comment save(Comment entity) {
		return commentRepository.save(entity);
	}

	@Override
	public void delete(Comment entity) {
		//deleteReplies(entity.getReplies());
		commentRepository.delete(entity);
	}

	@Override
	public List<Comment> findAll() {
		return commentRepository.findAll();
	}

	@Override
	public Optional<Comment> findById(Long id) {
		return commentRepository.findById(id);
	}

}
