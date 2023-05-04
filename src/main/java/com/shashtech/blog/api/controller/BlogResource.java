package com.shashtech.blog.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shashtech.blog.api.dto.PostDTO;
import com.shashtech.blog.api.response.MessageResponse;
import com.shashtech.blog.api.service.BlogServiceImpl;
import com.shashtech.blog.api.util.AppConstants;

@RestController
@RequestMapping("/api")
public class BlogResource {
	
	@Autowired
	private BlogServiceImpl blogServiceImpl;
	
	/**
	 * @param postDTO
	 * @return
	 */
	@PostMapping("/blog")
	public ResponseEntity<?> addBlog(@Valid @RequestBody PostDTO postDTO) {
		boolean status = blogServiceImpl.addBlog(postDTO);
		if(status) {
			return new ResponseEntity<>(new MessageResponse(AppConstants.POST_ADD_SUCCESS
					,HttpStatus.CREATED.value(),AppConstants.SUCCESS_STATUS,null),HttpStatus.CREATED);
		} 
			
		return new ResponseEntity<>(new MessageResponse(AppConstants.POST_ADD_FAILURE
				,HttpStatus.INTERNAL_SERVER_ERROR.value(),AppConstants.ERROR_STATUS,null),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PutMapping("/blog/{blogId}")
	public ResponseEntity<?> editBlog(@Valid @RequestBody PostDTO postDTO,@PathVariable Integer blogId) {
		postDTO.setBlogId(blogId);
		boolean status = blogServiceImpl.addBlog(postDTO);
		if(status) {
			return new ResponseEntity<>(new MessageResponse(AppConstants.POST_ADD_SUCCESS
					,HttpStatus.OK.value(),AppConstants.SUCCESS_STATUS,null),HttpStatus.OK);
		} 
			
		return new ResponseEntity<>(new MessageResponse(AppConstants.POST_ADD_FAILURE
				,HttpStatus.INTERNAL_SERVER_ERROR.value(),AppConstants.ERROR_STATUS,null),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/blog/{blogId}")
	public ResponseEntity<?> getBlogById(@PathVariable Integer blogId) {
		PostDTO blogById = blogServiceImpl.getBlogById(blogId);
		Map<String,Object> detail= new HashMap<>();
		detail.put("post", blogById);
		return new ResponseEntity<>(new MessageResponse(AppConstants.RECORD_FOUND
				,HttpStatus.OK.value(),AppConstants.SUCCESS_STATUS,detail),HttpStatus.OK);
	}
	
	@GetMapping("/blog")
	public ResponseEntity<?> getAllBlogs() {
		List<PostDTO> allPosts = blogServiceImpl.getAllPosts();
		Map<String,Object> detail= new HashMap<>();
		detail.put("posts", allPosts);
		return new ResponseEntity<>(new MessageResponse(AppConstants.RECORD_FOUND
				,HttpStatus.OK.value(),AppConstants.SUCCESS_STATUS,detail),HttpStatus.OK);
	}
}
