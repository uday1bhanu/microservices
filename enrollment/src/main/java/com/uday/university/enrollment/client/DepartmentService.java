/**
 * 
 */
package com.uday.university.enrollment.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.uday.university.enrollment.model.Course;
import com.uday.university.enrollment.model.Department;

/**
 * @author udaybhanuprasad
 *
 */
@Service("departmentService")
public class DepartmentService {
	Map<String, String> vars = new HashMap<String, String>();
    
	private RestTemplate restTemplate = new RestTemplate();
	private String serviceUrl;
	
	@Autowired
	public void DepartmentSercice(){
		this.serviceUrl = System.getenv("service.department.url");
	}
	
	public Department findByDepartmentId(ObjectId departmentId){
		vars = new HashMap<String, String>();
		vars.put("id", departmentId.toString());
		
		Department department = null;
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(serviceUrl+"/departments").queryParam("id", departmentId);
		
		ResponseEntity<Resource<Department>> responseEntity =
		  restTemplate.exchange(builder.buildAndExpand().toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<Resource<Department>>() {});
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
		 Resource<Department> userResource = responseEntity.getBody();
		 department = userResource.getContent();
		}
		
		return department;
	}
	
	public Department findByDepartmentCode(String departmentCode){
		vars = new HashMap<String, String>();
		vars.put("id", departmentCode);
		
		Department department = null;
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(serviceUrl+"/departments").queryParam("code", departmentCode);
		System.out.println(builder.buildAndExpand().toUri());
		ResponseEntity<Resource<Department>> responseEntity =
		  restTemplate.exchange(builder.buildAndExpand().toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<Resource<Department>>() {});
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
		 Resource<Department> userResource = responseEntity.getBody();
		 department = userResource.getContent();
		}
		return department;
	}
	
	public List<Course> findCoursesByDepartmentId(ObjectId departmentId){
		return findByDepartmentId(departmentId).getCourses();
	}
	
	public List<Course> findCoursesByDepartmentCode(String departmentCode){
		return findByDepartmentCode(departmentCode).getCourses();
	}
}
