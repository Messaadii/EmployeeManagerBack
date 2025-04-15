package com.elkhobna.employeemanager.controllers;

import com.elkhobna.employeemanager.dtos.Count;
import com.elkhobna.employeemanager.dtos.SimpleCategory;
import com.elkhobna.employeemanager.entities.Category;
import com.elkhobna.employeemanager.entities.Company;
import com.elkhobna.employeemanager.entities.Employee;
import com.elkhobna.employeemanager.entities.User;
import com.elkhobna.employeemanager.exceptions.BusinessException;
import com.elkhobna.employeemanager.repositories.CategoryRepository;
import com.elkhobna.employeemanager.repositories.CompanyRepository;
import com.elkhobna.employeemanager.repositories.DocumentRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/rest/category")
@RestController
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final DocumentRepository documentRepository;

    public CategoryController(CategoryRepository categoryRepository, DocumentRepository documentRepository) {
        this.categoryRepository = categoryRepository;
        this.documentRepository = documentRepository;
    }

    @PostMapping()
    public ResponseEntity<Category> save(@Valid @RequestBody Category category) {
        Optional<Category> fetchedCategory = categoryRepository.findById(category.getName());
        if(fetchedCategory.isPresent()){
            throw new BusinessException("There is an existing category with the name (" + category.getName() + ")");
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
        Long documents = documentRepository.countByCategory(category.getName());
        if(documents > 0){
            throw new BusinessException("This category is assigned to " + documents +
                    " document" + (documents == 1 ? "" : "s") + ". Please remove " + (documents == 1 ? "it" : "them") + " first.");
        }
        categoryRepository.deleteById(category.getName());
    }

    @GetMapping()
    public Page<Category> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryRepository.search(name, description, pageable);
    }

    @GetMapping("/simple-find")
    public List<SimpleCategory> simpleFind() {
        return categoryRepository.simpleFind();
    }

    @GetMapping("/count")
    public Count count() {
        return new Count(categoryRepository.count());
    }
}
