package com.elkhobna.employeemanager.repositories;

import com.elkhobna.employeemanager.dtos.SimpleCategory;
import com.elkhobna.employeemanager.dtos.SimpleCompany;
import com.elkhobna.employeemanager.entities.Company;
import com.elkhobna.employeemanager.entities.Document;
import com.elkhobna.employeemanager.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends CrudRepository<Company, String> {

    @Query("""
        SELECT c FROM com.elkhobna.employeemanager.entities.Company c
        WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
           and (:description IS NULL OR LOWER(c.description) LIKE LOWER(CONCAT('%', :description, '%')))
    """)
    Page<Company> search(@Param("name") String name,
                          @Param("description") String description,
                          Pageable pageable);

    @Query("""
        SELECT new com.elkhobna.employeemanager.dtos.SimpleCompany(c.name) 
        FROM com.elkhobna.employeemanager.entities.Company c
    """)
    List<SimpleCompany> simpleFind();

    @Query("""
        SELECT e.joiningDate, COUNT(e)
        FROM com.elkhobna.employeemanager.entities.Employee e
        LEFT JOIN e.company c
        WHERE c.name = :company
        GROUP BY c.name, e.joiningDate
        ORDER BY e.joiningDate DESC
   """)
    List<Object[]> countEmployeesByCompanyAndJoiningDate(@Param("company") String company, Pageable pageable);
}