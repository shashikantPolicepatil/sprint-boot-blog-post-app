package com.shashtech.blog.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shashtech.blog.api.entity.Comments;

public interface CommentRepository extends JpaRepository<Comments, Integer> {

}
