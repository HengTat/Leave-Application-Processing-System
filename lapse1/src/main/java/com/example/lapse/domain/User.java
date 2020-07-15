package com.example.lapse.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@MappedSuperclass
public abstract class User {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@NotEmpty
	private String name;
	@NotEmpty
	@Size(min=6, max=20)
	private String password;
	@NotEmpty
	@Email
	private String email;
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(@NotEmpty String name, @NotEmpty @Size(min=6, max=20) String password, @NotEmpty @Email String email) {
		super();
		this.name = name;
		this.password = password;
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password + ", email=" + email + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
