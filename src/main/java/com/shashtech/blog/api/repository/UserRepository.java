package com.shashtech.blog.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shashtech.blog.api.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public Optional<User> findByUserName(String username);
	
	public Optional<User> findByEmail(String email);

}
