/**
 * 
 */
package com.uday.university.enrollment.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.uday.university.enrollment.utility.ObjectIdJsonSerializer;

/**
 * @author udaybhanuprasad
 *
 */
@Document(collection="person")
public class Person {
	@Id
	@JsonSerialize(using = ObjectIdJsonSerializer.class)
	private ObjectId id;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String address;
	private String type;
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId departmentId;
	private List<String> courseCodes = new ArrayList<String>();
	private String status = "A";
	private Double gpa;
	private Date joinDate = new Date();
	
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ObjectId getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(ObjectId departmentId) {
		this.departmentId = departmentId;
	}
	public List<String> getCourseCodes() {
		return courseCodes;
	}
	public void setCourseCodes(List<String> courseCodes) {
		this.courseCodes = courseCodes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Double getGpa() {
		return gpa;
	}
	public void setGpa(Double gpa) {
		this.gpa = gpa;
	}
	public Date getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
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
