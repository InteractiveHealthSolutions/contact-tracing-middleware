package com.ihsinformatics.cad4tb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="form")
public class Form implements Serializable{
	
	private Integer id;
	private String form_type;
	private String form_json;
	private String userId;
	private String userName;
	private Boolean submitted;
	
	public Form(){}
	
	public Form(String type, String json, String uid, String uname, Boolean submitted){
		this.form_type = type;
		this.form_json = json;
		this.userId = uid;
		this.userName = uname;
		this.submitted = submitted;
	}
	
	@Column(name="user_sid")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name="user_name")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Column(name="submitted")
	public Boolean getSubmitted() {
		return submitted;
	}
	
	public void setSubmitted(Boolean submitted) {
		this.submitted = submitted;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="form_type")
	public String getForm_type() {
		return form_type;
	}
	public void setForm_type(String form_type) {
		this.form_type = form_type;
	}
	
	@Column(name="form_json")
	public String getForm_json() {
		return form_json;
	}
	public void setForm_json(String form_json) {
		this.form_json = form_json;
	}
}
