package com.shashtech.blog.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shashtech.blog.api.dto.CommentDTO;
import com.shashtech.blog.api.dto.PostDTO;
import com.shashtech.blog.api.response.MessageResponse;
import com.shashtech.blog.api.service.BlogServiceImpl;
import com.shashtech.blog.api.util.AppConstants;

@RestController
@RequestMapping("/api")
public class CommentResource {

	@Autowired
	private BlogServiceImpl blogServiceImpl;
	
	@PostMapping("/comment/{blogId}")
	public ResponseEntity<?> addComment(@Valid @RequestBody CommentDTO commentDTO,@PathVariable Integer blogId) {
		commentDTO.setPostId(blogId);
		boolean isAdded = blogServiceImpl.addComment(commentDTO);
		if(isAdded) {
			PostDTO blogById = blogServiceImpl.getBlogById(commentDTO.getPostId());
			Map<String,Object> detail= new HashMap<>();
			detail.put("post", blogById);
			return new ResponseEntity<>(new MessageResponse(AppConstants.COMMENT_ADD_SUCCESS
					,HttpStatus.CREATED.value(),AppConstants.SUCCESS_STATUS,detail),HttpStatus.CREATED);
		} 
		return new ResponseEntity<>(new MessageResponse(AppConstants.COMMENT_ADD_FAILURE
				,HttpStatus.INTERNAL_SERVER_ERROR.value(),AppConstants.ERROR_STATUS,null),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable Integer commentId) {
		boolean isDeleted = blogServiceImpl.removeComment(commentId);
		if(isDeleted) {
			return new ResponseEntity<>(new MessageResponse(AppConstants.COMMENT_REMOVE_SUCCESS
					,HttpStatus.OK.value(),AppConstants.SUCCESS_STATUS,null),HttpStatus.OK);
		}
		return new ResponseEntity<>(new MessageResponse(AppConstants.COMMENT_REMOVE_FAILURE
				,HttpStatus.INTERNAL_SERVER_ERROR.value(),AppConstants.ERROR_STATUS,null),HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
