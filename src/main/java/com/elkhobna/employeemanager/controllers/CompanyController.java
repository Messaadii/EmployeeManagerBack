package com.elkhobna.employeemanager.controllers;

import com.elkhobna.employeemanager.entities.Category;
import com.elkhobna.employeemanager.entities.Company;
import com.elkhobna.employeemanager.entities.Employee;
import com.elkhobna.employeemanager.repositories.CompanyRepository;
import com.elkhobna.employeemanager.repositories.EmployeeRepository;
import com.elkhobna.employeemanager.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/company")
@RestController
public class CompanyController {

    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @PostMapping()
    public ResponseEntity<Company> save(@Valid @RequestBody Company company) {
        Optional<Company> fetchedCompany = companyRepository.findById(company.getName());
        if(fetchedCompany.isPresent()){
            throw new RuntimeException("There is an existing company with the name (" + company.getName() + ")");
        }
        return ResponseEntity.ok(
                companyRepository.save(company)
        );
    }

    @PutMapping()
    public ResponseEntity<Company> update(@Valid @RequestBody Company company) {
        return ResponseEntity.ok(
                companyRepository.save(company)
        );
    }

    @DeleteMapping()
    public void delete(@RequestBody Company company) {
        companyRepository.delete(company);
    }
}
