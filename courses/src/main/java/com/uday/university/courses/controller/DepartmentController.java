/**
 * 
 */
package com.uday.university.courses.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


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

import com.uday.university.courses.model.Course;
import com.uday.university.courses.model.Department;
import com.uday.university.courses.resources.DepartmentResource;
import com.uday.university.courses.service.DepartmentService;

/**
 * @author udaybhanuprasad
 *
 */
@Controller
public class DepartmentController {
	@Autowired
	private DepartmentService departmentService;
	Logger logger = LoggerFactory.getLogger("com.uday.university.courses.DepartmentController");
	
	
	@RequestMapping(method=RequestMethod.GET, value="/healthz", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> healthCheck(){
		StringBuilder responseBody = new StringBuilder();
		responseBody.append("{\"health\": \"OK\"}");
        return new ResponseEntity<String>(responseBody.toString(), HttpStatus.OK);
	}
	@RequestMapping(method=RequestMethod.GET, value="/departments/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DepartmentResource>> findAll(){
		logger.debug("DepartmentController#findAll::: Received request for /departments");
		List<Department> departments = new ArrayList<Department>();
		departments = departmentService.findAllDepartments();
		
		if (departments == null) {
			logger.debug("DepartmentController#findAll::: Fix this! Need to send back list of all departments");
            return new ResponseEntity<List<DepartmentResource>>(HttpStatus.NOT_FOUND);
        }
		List<DepartmentResource> departmentResources = new ArrayList<DepartmentResource>();
		for(Department dep : departments){
			departmentResources.add(new DepartmentResource(dep));
		}
		logger.debug("DepartmentController#findAll::: Return response for /departments");
        return new ResponseEntity<List<DepartmentResource>>(departmentResources, null, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/departments", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DepartmentResource> findByIdOrCode(@RequestParam(value = "id", required=false) final ObjectId departmentId, @RequestParam(value = "code", required=false) final String departmentCode){
		logger.debug("DepartmentController#findByIdOrCode::: Received request for /departments");
		Department department = null;
		if(departmentId != null){
			logger.debug("DepartmentController#findByIdOrCode::: DepartmentId: "+ departmentId);
			department = departmentService.findDepartmentByDepartmentId(departmentId);
		}
		else if(null != departmentCode && !departmentCode.isEmpty()){
			logger.debug("DepartmentController#findByIdOrCode::: DepartmentCode: "+ departmentCode);
			department = departmentService.findDepartmentByDepartmentCode(departmentCode);
		}
		DepartmentResource departmentResource = new DepartmentResource(department);
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(departmentResource.getLink("self").getHref()));
        logger.debug("DepartmentController#findByIdOrCode::: Return response for /departments");
        return new ResponseEntity<DepartmentResource>(departmentResource, null, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/departments", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DepartmentResource> save(@RequestBody Department department){
		department = departmentService.save(department);
		
		DepartmentResource departmentResource = new DepartmentResource(department);
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(departmentResource.getLink("self").getHref()));
        
        return new ResponseEntity<DepartmentResource>(departmentResource, httpHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/departments/{id}/courses", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Course>> findAllCoursesByDepartmentId(@PathVariable("id") final ObjectId departmentId){
		List<Course> courses = departmentService.findAllCoursesByDepartmentId(departmentId);
		if (courses == null) {
            logger.debug("Courses for department id " + departmentId + " not found");
            return new ResponseEntity<List<Course>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Course>>(courses, HttpStatus.OK); 
	}
	
}
