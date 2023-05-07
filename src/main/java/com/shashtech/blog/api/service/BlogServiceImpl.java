package com.shashtech.blog.api.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shashtech.blog.api.dto.CommentDTO;
import com.shashtech.blog.api.dto.PostDTO;
import com.shashtech.blog.api.entity.Comments;
import com.shashtech.blog.api.entity.Posts;
import com.shashtech.blog.api.repository.CommentRepository;
import com.shashtech.blog.api.repository.PostRepository;
import com.shashtech.blog.api.util.AppConstants;
import com.shashtech.blog.api.util.CommonUtil;

@Service
public class BlogServiceImpl {

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CommentRepository commentRepository;

	public boolean addBlog(PostDTO postDTO) {
		try {
			UserDetailsServiceImpl userFromContext = CommonUtil.getUserFromContext();
			Posts newPost = Posts.builder()
					.description(postDTO.getDescription())
					.title(postDTO.getTitle())
					.content(postDTO.getContent())
					.build();
			newPost.getUser().setUserId(userFromContext.getId());
			if(postDTO.getBlogId()==null) {
				newPost.setCreatedOn(LocalDate.now());
			} else {
				newPost.setCreatedOn(postDTO.getCreatedOn());
				newPost.setPostId(postDTO.getBlogId());
				newPost.setUdpatedOn(LocalDate.now());
			}
			postRepository.save(newPost);
		} catch (Exception ex) {
			// log error
			return false;
		}
		return true;
	}
	
	public PostDTO getBlogById(Integer blogId) {
		Optional<Posts> optionalPost = postRepository.findById(blogId);
		if(optionalPost.isPresent()) {
			Posts post = optionalPost.get();
		PostDTO postDTO = new PostDTO(post.getPostId(), post.getContent(), post.getDescription(),
				post.getTitle(), post.getCreatedOn(), post.getUdpatedOn(), post.getCommentList(),
				AppConstants.BASE_URL+"/blog/"+post.getPostId());
		return postDTO;
		}
		throw new EntityNotFoundException("Blog is not found");
		
	}
	
	public List<PostDTO> getAllPosts() {
		UserDetailsServiceImpl userFromContext = CommonUtil.getUserFromContext();
		boolean isAdmin = userFromContext.getAuthorities().stream().anyMatch(auth->auth.getAuthority().equals("ROLE_ADMIN"));
		if(isAdmin) {
			List<Posts> findAll = postRepository.findAll();
			return CommonUtil.convertPostEntityToPostDTO(findAll);
		} else {
			List<Posts> findByUserId = postRepository.findByUserId(userFromContext.getId());
			return CommonUtil.convertPostEntityToPostDTO(findByUserId);
		}
	}

	public boolean addComment(CommentDTO commentDTO) {
		Optional<Posts> post = postRepository.findById(commentDTO.getPostId());
		if(post.isPresent()) {
			Comments newComment = Comments.builder().content(commentDTO.getContent())
					.email(commentDTO.getEmail())
					.name(commentDTO.getName())
					.creatdOn(LocalDate.now()).build();
			newComment.setPost(post.get());
			commentRepository.save(newComment);
			return true;
		}
		return false;
	}

	public boolean removeComment(Integer commentId) {
		commentRepository.deleteById(commentId);
		return true;
	}

	public boolean removeBlog(Integer postId) {
		postRepository.deleteById(postId);
		return false;
	}

}
