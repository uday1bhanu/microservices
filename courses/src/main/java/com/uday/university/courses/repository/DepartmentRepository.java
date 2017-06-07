/**
 * 
 */
package com.uday.university.courses.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.uday.university.courses.model.Department;

/**
 * @author udaybhanuprasad
 *
 */
@Repository("departmentRepository")
public interface DepartmentRepository extends MongoRepository<Department, ObjectId> {
	public Department findByName(String name);
	public Department findByCode(String code);
}
