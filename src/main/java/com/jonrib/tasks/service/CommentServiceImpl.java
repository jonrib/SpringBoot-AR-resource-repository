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

	@Override
	public Comment save(Comment entity) {
		return commentRepository.save(entity);
	}

	@Override
	public void delete(Comment entity) {
		//deleteReplies(entity.getReplies());
		commentRepository.delete(entity);
	}
	
	private void deleteReplies(Set<Comment> comments) {
		if (comments == null) {
			return;
		}
		for (Comment comment : comments) {
			deleteReplies(comment.getReplies());
			commentRepository.delete(comment);
		}
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
