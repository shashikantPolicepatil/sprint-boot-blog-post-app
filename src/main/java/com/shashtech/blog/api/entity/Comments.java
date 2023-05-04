package com.shashtech.blog.api.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="TBL_COMMENTS")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Comments {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer commentId;
	private String content;
	private String email;
	private String name;
	private LocalDate creatdOn;
	@ManyToOne
	@JoinColumn(name="post_id")
	private Posts post;
}
