package com.shashtech.blog.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shashtech.blog.api.entity.ERole;
import com.shashtech.blog.api.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	
	Optional<Role> findByName(ERole name);
	
}
