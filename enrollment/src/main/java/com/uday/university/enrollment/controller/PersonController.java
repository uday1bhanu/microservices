/**
 * 
 */
package com.uday.university.enrollment.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	Logger logger = LoggerFactory.getLogger("com.uday.university.enrollment.PersonController");
	
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
            logger.debug("Students not found");
            return new ResponseEntity<List<PersonResource>>(HttpStatus.NOT_FOUND);
        }
		
		List<PersonResource> personResources = new ArrayList<PersonResource>();
		for(Person student : students){
			personResources.add(new PersonResource(student));
		}
		
        return new ResponseEntity<List<PersonResource>>(personResources, null, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/students/random", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PersonResource>> findRandomStudent(){
		List<Person> students = new ArrayList<Person>();
		students = personService.findAllStudents();
		if (students == null) {
            logger.debug("Students not found");
            return new ResponseEntity<List<PersonResource>>(HttpStatus.NOT_FOUND);
        }
		
		List<PersonResource> personResources = new ArrayList<PersonResource>();
		
		personResources.add(new PersonResource(students.get((int)(Math.random() * ((students.size() - 1) + 1)))));
		
        return new ResponseEntity<List<PersonResource>>(personResources, null, HttpStatus.OK);
	}
	
	/*
	@RequestMapping(method=RequestMethod.GET, value="/students", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonResource> findStudentsById(@RequestParam(value = "id") final ObjectId studentId){
		Person person = personService.findStudentById(studentId);
		if (person == null) {
            logger.debug("StudentId " + studentId + " not found");
            return new ResponseEntity<PersonResource>(HttpStatus.NOT_FOUND);
        }
		PersonResource personResource = new PersonResource(person);
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(personResource.getLink("self").getHref()));
        
        return new ResponseEntity<PersonResource>(personResource, httpHeaders, HttpStatus.OK);
	}
	*/
	
	@RequestMapping(method=RequestMethod.GET, value="/professors", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PersonResource>> findProfessorsById(@RequestParam(value = "id") final ObjectId professorId){
		List<Person> professors = new ArrayList<Person>();
		if(professorId!=null){
			professors.add(personService.findProfessorById(professorId));
		}
		else {
			professors = personService.findAllProfessors();
		}
		if (professors == null) {
            logger.debug("Professors not found");
            return new ResponseEntity<List<PersonResource>>(HttpStatus.NOT_FOUND);
        }
		
		List<PersonResource> personResources = new ArrayList<PersonResource>();
		for(Person professor : professors){
			personResources.add(new PersonResource(professor));
		}
		
        return new ResponseEntity<List<PersonResource>>(personResources, null, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/professors/random", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PersonResource>> findRandomProfessor(){
		List<Person> professors = new ArrayList<Person>();
		professors = personService.findAllProfessors();
		if (professors == null) {
            logger.debug("Professors not found");
            return new ResponseEntity<List<PersonResource>>(HttpStatus.NOT_FOUND);
        }
		
		List<PersonResource> personResources = new ArrayList<PersonResource>();
		
		personResources.add(new PersonResource(professors.get((int)(Math.random() * ((professors.size() - 1) + 1)))));
		
        return new ResponseEntity<List<PersonResource>>(personResources, null, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/students", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PersonResource>> createStudent(@RequestBody List<Person> persons){
		for(Person person : persons){
			person.setType("S");	
		}
		persons = (List<Person>) personService.save(persons);
		List<PersonResource> personResources = new ArrayList<PersonResource>();
		for(Person person : persons){
			PersonResource personResource = new PersonResource(person);
			
	        personResources.add(personResource);
		}
		HttpHeaders httpHeaders = new HttpHeaders();
		if(persons.size() == 1){
			httpHeaders.setLocation(URI.create(personResources.get(0).getLink("self").getHref()));
		}
		else{
			httpHeaders = null;
		}
        return new ResponseEntity<List<PersonResource>>(personResources, httpHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/students/{id}/enroll", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonResource> enrollDepartment(@PathVariable("id") ObjectId studentId, @RequestBody Department department) throws Exception{
		Person person = null;
		String departmentCode = department.getCode();
		logger.debug("PersonController#enrollDepartment::: DepartmentCode: "+ departmentCode);
		logger.debug("PersonController#enrollDepartment::: Calling departmentService#findByDepartmentCode ");
		department = departmentService.findByDepartmentCode(departmentCode);
		logger.debug("PersonController#enrollDepartment::: Returned from departmentService#findByDepartmentCode ");
		if(department == null){
			throw new Exception("Department" + departmentCode + "not found");
		}
		logger.debug("DepartmentId: "+ department.getId() + " for "+ departmentCode);
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
	public ResponseEntity<List<PersonResource>> createProfessor(@RequestBody List<Person> persons){
		for(Person person : persons){
			person.setType("P");	
		}
		persons = (List<Person>) personService.save(persons);
		List<PersonResource> personResources = new ArrayList<PersonResource>();
		for(Person person : persons){
			PersonResource personResource = new PersonResource(person);
			
	        personResources.add(personResource);
		}
		HttpHeaders httpHeaders = new HttpHeaders();
		if(persons.size() == 1){
			httpHeaders.setLocation(URI.create(personResources.get(0).getLink("self").getHref()));
		}
		else{
			httpHeaders = null;
		}
        return new ResponseEntity<List<PersonResource>>(personResources, httpHeaders, HttpStatus.CREATED);
	}
}
