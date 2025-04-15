package com.elkhobna.employeemanager.repositories;

import com.elkhobna.employeemanager.dtos.SimpleCategory;
import com.elkhobna.employeemanager.entities.Category;
import com.elkhobna.employeemanager.entities.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, String> {

    @Query("""
        SELECT c FROM com.elkhobna.employeemanager.entities.Category c
        WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
           and (:description IS NULL OR LOWER(c.description) LIKE LOWER(CONCAT('%', :description, '%')))
    """)
    Page<Category> search(@Param("name") String name,
                          @Param("description") String description, Pageable pageable);

    @Query("""
        SELECT new com.elkhobna.employeemanager.dtos.SimpleCategory(c.name) 
        FROM com.elkhobna.employeemanager.entities.Category c
    """)
    List<SimpleCategory> simpleFind();
}