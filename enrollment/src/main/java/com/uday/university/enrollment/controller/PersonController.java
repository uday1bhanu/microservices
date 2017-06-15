/**
 * 
 */
package com.uday.university.enrollment.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.uday.university.enrollment.client.DepartmentService;
import com.uday.university.enrollment.model.Course;
import com.uday.university.enrollment.model.Department;
import com.uday.university.enrollment.model.Person;
import com.uday.university.enrollment.resources.PersonResource;
import com.uday.university.enrollment.service.PersonService;

/**
 * @author udaybhanuprasad
 *
 */
@Controller
public class PersonController {
	@Autowired
	PersonService personService;
	@Autowired
	DepartmentService departmentService;
	
	@RequestMapping(method=RequestMethod.GET, value="/healthz", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> healthCheck(){
		StringBuilder responseBody = new StringBuilder();
		responseBody.append("{\"health\": \"OK\"}");
        return new ResponseEntity<String>(responseBody.toString(), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/students", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PersonResource>> findStudents(@RequestParam(value = "id", required=false) final ObjectId studentId){
		List<Person> students = new ArrayList<Person>();
		if(studentId!=null){
			students.add(personService.findStudentById(studentId));
		}
		else {
			students = personService.findAllStudents();
		}
		if (students == null) {
            System.out.println("Students not found");
            return new ResponseEntity<List<PersonResource>>(HttpStatus.NOT_FOUND);
        }
		
		List<PersonResource> personResources = new ArrayList<PersonResource>();
		for(Person student : students){
			personResources.add(new PersonResource(student));
		}
		
        return new ResponseEntity<List<PersonResource>>(personResources, null, HttpStatus.OK);
	}
	
	/*
	@RequestMapping(method=RequestMethod.GET, value="/students", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonResource> findStudentsById(@RequestParam(value = "id") final ObjectId studentId){
		Person person = personService.findStudentById(studentId);
		if (person == null) {
            System.out.println("StudentId " + studentId + " not found");
            return new ResponseEntity<PersonResource>(HttpStatus.NOT_FOUND);
        }
		PersonResource personResource = new PersonResource(person);
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(personResource.getLink("self").getHref()));
        
        return new ResponseEntity<PersonResource>(personResource, httpHeaders, HttpStatus.OK);
	}
	*/
	
	@RequestMapping(method=RequestMethod.GET, value="/professors", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonResource> findProfessorsById(@RequestParam(value = "id") final ObjectId professorId){
		Person person = personService.findProfessorById(professorId);
		if (person == null) {
            System.out.println("ProfessorId " + professorId + " not found");
            return new ResponseEntity<PersonResource>(HttpStatus.NOT_FOUND);
        }
		PersonResource personResource = new PersonResource(person);
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(personResource.getLink("self").getHref()));
        
        return new ResponseEntity<PersonResource>(personResource, httpHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/students", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonResource> createStudent(@RequestBody Person person){
		person.setType("S");
		person = personService.save(person);
		
		PersonResource personResource = new PersonResource(person);
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(personResource.getLink("self").getHref()));
        
        return new ResponseEntity<PersonResource>(personResource, httpHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/students/{id}/enroll", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonResource> enrollDepartment(@PathVariable("id") ObjectId studentId, @RequestBody Department department) throws Exception{
		Person person = null;
		String departmentCode = department.getCode();
		System.out.println("PersonController#enrollDepartment::: DepartmentCode: "+ departmentCode);
		System.out.println("PersonController#enrollDepartment::: Calling departmentService#findByDepartmentCode ");
		department = departmentService.findByDepartmentCode(departmentCode);
		System.out.println("PersonController#enrollDepartment::: Returned from departmentService#findByDepartmentCode ");
		if(department == null){
			throw new Exception("Department" + departmentCode + "not found");
		}
		System.out.println("DepartmentId: "+ department.getId() + " for "+ departmentCode);
		try {
			person = personService.enrollDepartment(studentId, department.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PersonResource personResource = new PersonResource(person);
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(personResource.getLink("self").getHref()));
        
        return new ResponseEntity<PersonResource>(personResource, httpHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/students/{id}/register", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonResource> registerCourse(@PathVariable("id") ObjectId studentId, @RequestBody Course course) throws Exception{
		if(course.getCode() == null ||course.getCode().isEmpty()){
			throw new Exception("Course Code shouldnt be empty");
		}
		
		Person person = personService.findStudentById(studentId);
		List<Course> courses = departmentService.findCoursesByDepartmentId(person.getDepartmentId());
		boolean match = false;
		for(Course c : courses){
			if(course.getCode().equals(c.getCode())){
				person = personService.enrollCourse(studentId, c.getCode());
			}
		}
		
		if(!match){
			throw new Exception("Course "+ course.getCode()+" doesnt exist in your department!");
		}
		
		PersonResource personResource = new PersonResource(person);
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(personResource.getLink("self").getHref()));
        
        return new ResponseEntity<PersonResource>(personResource, httpHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/professors/{id}/enroll", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonResource> joinDepartment(@PathVariable("id") ObjectId professorId, @RequestBody Department department){
		Person person = null;
		department = departmentService.findByDepartmentCode(department.getCode());
		try {
			person = personService.enrollDepartment(professorId, department.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PersonResource personResource = new PersonResource(person);
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(personResource.getLink("self").getHref()));
        
        return new ResponseEntity<PersonResource>(personResource, httpHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/professors/{id}/teach", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonResource> teachCourse(@PathVariable("id") ObjectId professorId, @RequestBody Course course) throws Exception{
		if(course.getCode() == null ||course.getCode().isEmpty()){
			throw new Exception("Course Code shouldnt be empty");
		}
		
		Person person = personService.findProfessorById(professorId);
		List<Course> courses = departmentService.findCoursesByDepartmentId(person.getDepartmentId());
		boolean match = false;
		for(Course c : courses){
			if(course.getCode().equals(c.getCode())){
				person = personService.teachCourse(professorId, c.getCode());
			}
		}
		
		if(!match){
			throw new Exception("Course "+ course.getCode()+" doesnt exist in your department!");
		}
		
		PersonResource personResource = new PersonResource(person);
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(personResource.getLink("self").getHref()));
        
        return new ResponseEntity<PersonResource>(personResource, httpHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/professors", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonResource> createProfessor(@RequestBody Person person){
		person.setType("P");
		person = personService.save(person);
		
		PersonResource personResource = new PersonResource(person);
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(personResource.getLink("self").getHref()));
        
        return new ResponseEntity<PersonResource>(personResource, httpHeaders, HttpStatus.CREATED);
	}
}
