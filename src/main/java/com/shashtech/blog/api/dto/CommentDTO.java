package com.shashtech.blog.api.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotBlank
	private String content;
	@NotBlank
	@Email
	private String email;
	@NotBlank
	private String name;
	
	private Integer postId;
}
