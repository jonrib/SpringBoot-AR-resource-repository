package com.jonrib.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonrib.app.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
