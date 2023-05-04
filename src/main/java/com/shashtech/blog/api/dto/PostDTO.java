package com.shashtech.blog.api.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.shashtech.blog.api.entity.Comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private Integer blogId;
	@NotBlank
	private String content;
	
	private String description;
	@NotBlank
	private String title;
	
	private LocalDate createdOn;
	
	private LocalDate updatedOn;
	
	private List<Comments> comments;
	
	private String uri;
}
