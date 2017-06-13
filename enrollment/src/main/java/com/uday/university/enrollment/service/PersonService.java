/**
 * 
 */
package com.uday.university.enrollment.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.uday.university.enrollment.model.Person;

/**
 * @author udaybhanuprasad
 *
 */
public interface PersonService {
	Person save(Person person);
	Person enrollDepartment(ObjectId personId, ObjectId departmentId) throws Exception;
	Person enrollCourse(ObjectId personId, String courseCode) throws Exception;
	Person teachCourse(ObjectId personId, String courseCode) throws Exception;
	Person findStudentById(ObjectId studentId);
	Person findStudentByFirstNameAndLastName(String firstName, String lastName);
	Person findStudentByEmailAddress(String emailAddress);
	public List<Person> findStudentsByDepartmentCode(String departmentCode);
	Person findProfessorById(ObjectId professorId);
	Person findProfessorByFirstNameAndLastName(String firstName, String lastName);
	Person findProfessorByEmailAddress(String emailAddress);
	public List<Person> findProfessorsByDepartmentCode(String departmentCode);
}
