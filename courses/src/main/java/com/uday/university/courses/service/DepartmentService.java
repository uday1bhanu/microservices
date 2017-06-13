/**
 * 
 */
package com.uday.university.courses.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.uday.university.courses.model.Course;
import com.uday.university.courses.model.Department;

/**
 * @author udaybhanuprasad
 *
 */
public interface DepartmentService {
	Department save(Department department);
	Department addCourse(ObjectId departmentId, Course course) throws Exception;
	List<Department> findAllDepartments();
	Department findDepartmentByDepartmentId(ObjectId departmentId);
	List<Course> findAllCoursesByDepartmentId(ObjectId departmentId);
	Department findDepartmentByDepartmentCode(String departmentCode);
	List<Course> findAllCoursesByDepartmentCode(String departmentCode);
}
