package com.jonrib.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonrib.tasks.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
