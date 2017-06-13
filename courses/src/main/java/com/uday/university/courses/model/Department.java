/**
 * 
 */
package com.uday.university.courses.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.uday.university.courses.utility.ObjectIdJsonSerializer;

/**
 * @author udaybhanuprasad
 *
 */
@Document(collection="department")
public class Department {
	@Id
	@JsonSerialize(using = ObjectIdJsonSerializer.class)
	private ObjectId id;
	private String name;
	private String code;
	private List<Course> courses = new ArrayList<Course>();
	
	@CreatedDate
	private Date createdDate = new Date();
	@LastModifiedDate
	private Date lastModifiedDate = new Date();
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<Course> getCourses() {
		return courses;
	}
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
