package com.elkhobna.employeemanager.repositories;

import com.elkhobna.employeemanager.entities.Category;
import com.elkhobna.employeemanager.entities.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, String> {
}