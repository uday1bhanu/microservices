/**
 * 
 */
package com.uday.university.enrollment.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uday.university.enrollment.client.DepartmentService;
import com.uday.university.enrollment.model.Person;
import com.uday.university.enrollment.repository.PersonRepository;

/**
 * @author udaybhanuprasad
 *
 */
@Service("personService")
public class PersonServiceImpl implements PersonService {

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	DepartmentService departmentService;
	
	/* (non-Javadoc)
	 * @see com.uday.university.enrollment.service.PersonService#save(com.uday.university.enrollment.model.Person)
	 */
	@Override
	public Iterable<Person> save(Iterable<Person> persons) {
		return personRepository.save(persons);
	}

	/* (non-Javadoc)
	 * @see com.uday.university.enrollment.service.PersonService#enrollDepartment(org.bson.types.ObjectId, org.bson.types.ObjectId)
	 */
	@Override
	public Person enrollDepartment(ObjectId personId, ObjectId departmentId) throws Exception {
		Person person = personRepository.findOne(personId);
		person.setDepartmentId(departmentId);
		person.setCourseCodes(null);//reset courses if changing department
		return personRepository.save(person);
	}

	/* (non-Javadoc)
	 * @see com.uday.university.enrollment.service.PersonService#enrollCourse(org.bson.types.ObjectId, org.bson.types.ObjectId)
	 */
	@Override
	public Person enrollCourse(ObjectId personId, String courseCode) throws Exception {
		Person person = personRepository.findOne(personId);
		if(person.getType().equals("S")){
			List<String> courses = new ArrayList<String>();
			courses.add(courseCode);
			person.setCourseCodes(courses);
			return personRepository.save(person);
		}
		else{
			throw new Exception("Must be a student to enroll in a course!");
		}
	}

	/* (non-Javadoc)
	 * @see com.uday.university.enrollment.service.PersonService#teachCourse(org.bson.types.ObjectId, org.bson.types.ObjectId)
	 */
	@Override
	public Person teachCourse(ObjectId personId, String courseCode) throws Exception {
		Person person = personRepository.findOne(personId);
		if(person.getType().equals("P")){
			List<String> courses = new ArrayList<String>();
			courses.add(courseCode);
			person.setCourseCodes(courses);
			return personRepository.save(person);
		}
		else{
			throw new Exception("Must be a professor to teach a course!");
		}
	}

	/* (non-Javadoc)
	 * @see com.uday.university.enrollment.service.PersonService#findStudentsByDepartmentCode(java.lang.String)
	 */
	@Override
	public List<Person> findStudentsByDepartmentCode(String departmentCode) {
		return personRepository.findByDepartmentIdAndType(departmentService.findByDepartmentCode(departmentCode).getId(), "S");
	}

	/* (non-Javadoc)
	 * @see com.uday.university.enrollment.service.PersonService#findProfessorsByDepartmentCode(java.lang.String)
	 */
	@Override
	public List<Person> findProfessorsByDepartmentCode(String departmentCode) {
		return personRepository.findByDepartmentIdAndType(departmentService.findByDepartmentCode(departmentCode).getId(), "P");
	}

	/* (non-Javadoc)
	 * @see com.uday.university.enrollment.service.PersonService#findStudentById(org.bson.types.ObjectId)
	 */
	@Override
	public Person findStudentById(ObjectId studentId) {
		return personRepository.findByIdAndType(studentId, "S");
	}

	/* (non-Javadoc)
	 * @see com.uday.university.enrollment.service.PersonService#findStudentByFirstNameAndLastName(java.lang.String, java.lang.String)
	 */
	@Override
	public Person findStudentByFirstNameAndLastName(String firstName, String lastName) {
		return personRepository.findByFirstNameAndLastNameAndType(firstName, lastName, "S");
	}

	/* (non-Javadoc)
	 * @see com.uday.university.enrollment.service.PersonService#findStudentByEmailAddress(java.lang.String)
	 */
	@Override
	public Person findStudentByEmailAddress(String emailAddress) {
		return personRepository.findByEmailAddressAndType(emailAddress, "S");
	}

	/* (non-Javadoc)
	 * @see com.uday.university.enrollment.service.PersonService#findProfessorById(org.bson.types.ObjectId)
	 */
	@Override
	public Person findProfessorById(ObjectId professorId) {
		return personRepository.findByIdAndType(professorId, "P");
	}

	/* (non-Javadoc)
	 * @see com.uday.university.enrollment.service.PersonService#findProfessorByFirstNameAndLastName(java.lang.String, java.lang.String)
	 */
	@Override
	public Person findProfessorByFirstNameAndLastName(String firstName, String lastName) {
		return personRepository.findByFirstNameAndLastNameAndType(firstName, lastName, "P");
	}

	/* (non-Javadoc)
	 * @see com.uday.university.enrollment.service.PersonService#findProfessorByEmailAddress(java.lang.String)
	 */
	@Override
	public Person findProfessorByEmailAddress(String emailAddress) {
		return personRepository.findByEmailAddressAndType(emailAddress, "P");
	}

	/* (non-Javadoc)
	 * @see com.uday.university.enrollment.service.PersonService#findAllStudents()
	 */
	@Override
	public List<Person> findAllStudents() {
		return personRepository.findAllByType("S");
	}

	/* (non-Javadoc)
	 * @see com.uday.university.enrollment.service.PersonService#findAllProfessors()
	 */
	@Override
	public List<Person> findAllProfessors() {
		return personRepository.findAllByType("P");
	}

}
