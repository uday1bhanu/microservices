/**
 * 
 */
package com.uday.university.courses.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uday.university.courses.model.Course;
import com.uday.university.courses.model.Department;
import com.uday.university.courses.repository.DepartmentRepository;

/**
 * @author udaybhanuprasad
 *
 */
@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;
	Logger logger = LoggerFactory.getLogger("com.uday.university.courses.DepartmentServiceImpl");
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.uday.university.courses.service.DepartmentService#findAllDepartments(
	 * )
	 */
	@Override
	public List<Department> findAllDepartments() {
		return departmentRepository.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uday.university.courses.service.DepartmentService#save(com.uday.
	 * university.courses.model.Department)
	 */
	@Override
	public Department save(Department department) {
		return departmentRepository.save(department);
	}

	/* (non-Javadoc)
	 * @see com.uday.university.courses.service.DepartmentService#addCourse(org.bson.types.ObjectId, com.uday.university.courses.model.Course)
	 */
	@Override
	public Department addCourse(ObjectId departmentId, Course course) throws Exception {
		Department department = departmentRepository.findOne(departmentId);
		if(department==null){
			throw new Exception("Department "+ departmentId + " not found!");
		}
		
		List<Course> courses = department.getCourses();
		if(courses==null){
			courses = new ArrayList<Course>();
		}
		courses.add(course);
		department.setCourses(courses);
		
		return departmentRepository.save(department);
	}

	/* (non-Javadoc)
	 * @see com.uday.university.courses.service.DepartmentService#findDepartmentByDepartmentId(org.bson.types.ObjectId)
	 */
	@Override
	public Department findDepartmentByDepartmentId(ObjectId departmentId) {
		return departmentRepository.findOne(departmentId);
	}

	/* (non-Javadoc)
	 * @see com.uday.university.courses.service.DepartmentService#findAllCoursesByDepartmentId(org.bson.types.ObjectId)
	 */
	@Override
	public List<Course> findAllCoursesByDepartmentId(ObjectId departmentId) {
		return departmentRepository.findOne(departmentId).getCourses();
	}

	/* (non-Javadoc)
	 * @see com.uday.university.courses.service.DepartmentService#findDepartmentByDepartmentCode(java.lang.String)
	 */
	@Override
	public Department findDepartmentByDepartmentCode(String departmentCode) {
		logger.debug("DepartmentService#findDepartmentByDepartmentCode::: DepartmentCode: "+ departmentCode);
		return departmentRepository.findByCode(departmentCode);
	}

	/* (non-Javadoc)
	 * @see com.uday.university.courses.service.DepartmentService#findAllCoursesByDepartmentCode(java.lang.String)
	 */
	@Override
	public List<Course> findAllCoursesByDepartmentCode(String departmentCode) {
		return departmentRepository.findByCode(departmentCode).getCourses();
	}
}
