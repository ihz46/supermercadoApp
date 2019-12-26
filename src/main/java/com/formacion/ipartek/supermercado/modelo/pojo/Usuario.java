package com.formacion.ipartek.supermercado.modelo.pojo;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class Usuario {
	
	private int id;
	
	@NotBlank
	@Size(min=2,max=50)
	private String user_name;
	
	@NotBlank
	@Size(min=2,max=50)
	private String password;
	
	
	

	public Usuario() {
		super();
		this.id = 0;
		this.user_name = "";
		this.password= "";
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", user_name=" + user_name + ", password=" + password + "]";
	}
	
	
	
}
