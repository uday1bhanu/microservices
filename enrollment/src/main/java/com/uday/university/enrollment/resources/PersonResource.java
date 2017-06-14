/**
 * 
 */
package com.uday.university.enrollment.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceSupport;

import com.uday.university.enrollment.controller.PersonController;
import com.uday.university.enrollment.model.Person;

/**
 * @author udaybhanuprasad
 *
 */
public class PersonResource extends ResourceSupport {
	private final Person person;
	
	public PersonResource(final Person person) {
        this.person = person;
        //this.add(new Link("htgd", "department-link"));
        //this.add(linkTo(DepartmentController.class).withRel("bookmarks"));
        if(person.getType().equals("S")){
        	this.add(linkTo(methodOn(PersonController.class).findStudents(person.getId())).withSelfRel());
        }
        else if(person.getType().equals("P")){
        	this.add(linkTo(methodOn(PersonController.class).findProfessorsById(person.getId())).withSelfRel());
        }
        //this.add(new Link(linkTo(methodOn(PersonController.class).findAllCoursesByDepartmentId(person.getId())).withSelfRel().getHref(), "courses"));
    }
    
    public Person getPerson(){
        return person;
    }
}
