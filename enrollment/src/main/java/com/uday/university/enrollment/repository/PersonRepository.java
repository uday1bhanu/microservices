/**
 * 
 */
package com.uday.university.enrollment.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.uday.university.enrollment.model.Person;

/**
 * @author udaybhanuprasad
 *
 */
public interface PersonRepository extends MongoRepository<Person, ObjectId> {
	public Person findByFirstNameAndLastName(String firstName, String lastName);
	public Person findByIdAndType(ObjectId id, String type);
	public Person findByEmailAddressAndType(String emailAddress, String type);
	public Person findByFirstNameAndLastNameAndType(String firstName, String lastName, String type);
	public Person findByEmailAddress(String emailAddress);
	public List<Person> findByDepartmentId(ObjectId departmentId);
	public List<Person> findByDepartmentIdAndType(ObjectId departmentId, String type);
	public List<Person> findAllByType(String type);
}
