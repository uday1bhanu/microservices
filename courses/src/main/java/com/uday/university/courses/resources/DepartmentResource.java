/**
 * 
 */
package com.uday.university.courses.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.uday.university.courses.controller.DepartmentController;
import com.uday.university.courses.model.Department;

/**
 * @author udaybhanuprasad
 *
 */
public class DepartmentResource extends ResourceSupport {
	private final Department department;
	
	public DepartmentResource(final Department department) {
        this.department = department;
        //this.add(new Link("htgd", "department-link"));
        //this.add(linkTo(DepartmentController.class).withRel("bookmarks"));
        this.add(linkTo(methodOn(DepartmentController.class).findByIdOrCode(department.getId(), department.getCode())).withSelfRel());
        this.add(new Link(linkTo(methodOn(DepartmentController.class).findAllCoursesByDepartmentId(department.getId())).withSelfRel().getHref(), "courses"));
    }
    
    public Department getDepartment(){
        return department;
    }
}
