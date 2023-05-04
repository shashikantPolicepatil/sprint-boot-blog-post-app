package com.shashtech.blog.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.shashtech.blog.api.entity.ERole;
import com.shashtech.blog.api.entity.Role;
import com.shashtech.blog.api.entity.User;
import com.shashtech.blog.api.repository.RoleRepository;
import com.shashtech.blog.api.repository.UserRepository;

@SpringBootApplication
public class BlogDemoApplication {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(BlogDemoApplication.class, args);
	}

	@PostConstruct
	public void initUsers() {
		 List<Role> roles = Stream.of(new Role(0, ERole.ROLE_ADMIN),new Role(0, ERole.ROLE_USER),
				new Role(0, ERole.ROLE_MODERATOR)).collect(Collectors.toList());
		 roleRepo.saveAll(roles);
		Role role = new Role(1,ERole.ROLE_ADMIN);
		Set<Role> roleSet = new HashSet<>();roleSet.add(role);
		List<User> usersList = Stream.of(new User(101,"dfkj","","name","password","email",roleSet),
				new User(102,"fjkg","","name2","password","email",roleSet)
				,new User(103,"dfkhlj","","name3","password","email",roleSet),
				new User(104,"dkljh","","name4","password","email",roleSet)).collect(Collectors.toList());
		repository.saveAll(usersList);
	}
	
	
}
