
package com.shashtech.blog.api.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="TBL_USER")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer userId;
	@NotBlank
	private String fName;
	private String lName;
	@NotBlank
	private String userName;
	@NotBlank
	@JsonIgnore
	private String password;
	@NotBlank
	private String email;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="tbl_user_roles" , joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	Set<Role> userRoles = new HashSet<>();
	
	@OneToMany(mappedBy="user", cascade=CascadeType.REMOVE)
	private List<Posts> postList;
	
	public User(int id,String fname,String lname,String userName,String password,String email,Set<Role> roles) {
		this.userId=id;this.fName=fname;this.lName=lname;this.userName=userName;
		this.password=password;this.email=email;this.userRoles=roles;
	}
}
