package com.elkhobna.employeemanager.repositories;

import com.elkhobna.employeemanager.entities.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, String> {

}