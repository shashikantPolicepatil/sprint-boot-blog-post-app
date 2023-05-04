package com.shashtech.blog.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shashtech.blog.api.entity.Posts;

public interface PostRepository extends JpaRepository<Posts, Integer> {
	
	@Query("from Posts p where p.user.userId=:userId")
	public List<Posts> findByUserId(@Param(value = "userId") Integer userId);

}
