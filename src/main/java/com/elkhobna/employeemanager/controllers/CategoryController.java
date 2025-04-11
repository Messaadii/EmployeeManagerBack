package com.elkhobna.employeemanager.controllers;

import com.elkhobna.employeemanager.entities.Category;
import com.elkhobna.employeemanager.entities.Company;
import com.elkhobna.employeemanager.entities.User;
import com.elkhobna.employeemanager.repositories.CategoryRepository;
import com.elkhobna.employeemanager.repositories.CompanyRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/category")
@RestController
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostMapping()
    public ResponseEntity<Category> save(@Valid @RequestBody Category category) {
        Optional<Category> fetchedCategory = categoryRepository.findById(category.getName());
        if(fetchedCategory.isPresent()){
            throw new RuntimeException("There is an existing category with the name (" + category.getName() + ")");
        }
        return ResponseEntity.ok(
                categoryRepository.save(category)
        );
    }

    @PutMapping()
    public ResponseEntity<Category> update(@Valid @RequestBody Category category) {
        return ResponseEntity.ok(
                categoryRepository.save(category)
        );
    }

    @DeleteMapping()
    public void delete(@RequestBody Category category) {
        categoryRepository.delete(category);
    }
}
