package com.shashtech.blog.api.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="TBL_POSTS")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Posts {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer postId;
	@Lob
	private String content;
	private String description;
	private String title;
	@CreationTimestamp
	private LocalDate createdOn;
	@CreationTimestamp
	private LocalDate udpatedOn;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	@Builder.Default
	private User user = new User();
	
	@OneToMany(mappedBy="post",cascade=CascadeType.REMOVE,fetch=FetchType.LAZY)
	@Builder.Default
	private List<Comments> commentList= new ArrayList<>();
}
