package com.elkhobna.employeemanager.repositories;

import com.elkhobna.employeemanager.dtos.SimpleEmployee;
import com.elkhobna.employeemanager.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, String> {

    @Query("""
        SELECT e FROM com.elkhobna.employeemanager.entities.Employee e
        LEFT JOIN e.company c
        WHERE (:cin IS NULL OR LOWER(e.cin) LIKE LOWER(CONCAT('%', CAST(:cin as text), '%')))
           and (:id IS NULL OR e.id = :id)
           and (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', CAST(:name as text), '%')))
           and (:familyName IS NULL OR LOWER(e.familyName) LIKE LOWER(CONCAT('%', CAST(:familyName as text), '%')))
           and (:company IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', CAST(:company as text), '%')))
           and (:joiningDate IS NULL OR e.joiningDate = :joiningDate)
    """)
    Page<Employee> search(@Param(value = "cin") String cin,
                          @Param(value = "id") Long id,
                          @Param(value = "name") String name,
                          @Param(value = "familyName") String familyName,
                          @Param(value = "company") String company,
                          @Param(value = "joiningDate") LocalDate joiningDate,
                          Pageable pageable);

    @Query("""
        SELECT e FROM com.elkhobna.employeemanager.entities.Employee e
        WHERE e.id = :id
    """)
    Optional<Employee> findByID(@Param(value = "id") Long id);

    @Query("""
        SELECT new com.elkhobna.employeemanager.dtos.SimpleEmployee(e.id, e.cin, e.name, e.familyName) 
        FROM com.elkhobna.employeemanager.entities.Employee e
    """)
    List<SimpleEmployee> simpleFind();

    @Query("""
        SELECT COUNT(e) 
        FROM com.elkhobna.employeemanager.entities.Employee e
        LEFT JOIN e.company c
        WHERE c.name = :name
    """)
    Long countByCompany(@Param("name") String name);
}