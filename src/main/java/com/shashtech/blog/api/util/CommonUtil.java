/**
 * 
 */
package com.shashtech.blog.api.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.shashtech.blog.api.dto.PostDTO;
import com.shashtech.blog.api.entity.Posts;
import com.shashtech.blog.api.service.UserDetailsServiceImpl;

/**
 * @author User
 *
 */
public class CommonUtil {
	
	
	public static UserDetailsServiceImpl getUserFromContext(){
		 Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 if(principal!=null && principal instanceof UserDetails)
	         return (UserDetailsServiceImpl)principal;
		return null;
	}
	
	public static List<PostDTO> convertPostEntityToPostDTO(List<Posts> findAll) {
		List<PostDTO> response = findAll.stream()
		.map(post-> new PostDTO(post.getPostId(),
				null, null, post.getTitle(), post.getCreatedOn(), post.getUdpatedOn(), null,
				AppConstants.BASE_URL+"/blog/"+post.getPostId()))
		.collect(Collectors.toList());
		return response;
	}

}
