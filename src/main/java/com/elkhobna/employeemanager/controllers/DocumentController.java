package com.elkhobna.employeemanager.controllers;

import com.elkhobna.employeemanager.dtos.Count;
import com.elkhobna.employeemanager.entities.Company;
import com.elkhobna.employeemanager.entities.Document;
import com.elkhobna.employeemanager.entities.Employee;
import com.elkhobna.employeemanager.exceptions.BusinessException;
import com.elkhobna.employeemanager.repositories.DocumentRepository;
import com.elkhobna.employeemanager.repositories.EmployeeRepository;
import com.elkhobna.employeemanager.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/rest/document")
@RestController
public class DocumentController {

    private final DocumentRepository documentRepository;
    private final EmployeeRepository employeeRepository;
    private final AuthenticationService authenticationService;

    public DocumentController(DocumentRepository documentRepository, AuthenticationService authenticationService, EmployeeRepository employeeRepository) {
        this.documentRepository = documentRepository;
        this.authenticationService = authenticationService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping()
    public ResponseEntity<Document> save(@Valid @RequestBody Document document) {
        if(document.getImageName() == null
            || document.getImageBase64() == null){
            throw new BusinessException("Please attach an image to the document");
        }
        checkEligibility(document, "add");
        return ResponseEntity.ok(
                documentRepository.save(document)
        );
    }

    @PutMapping()
    public ResponseEntity<Document> update(@Valid @RequestBody Document document) {
        checkEligibility(document, "update");

        return ResponseEntity.ok(
                documentRepository.save(document)
        );
    }

    @DeleteMapping()
    public void delete(@RequestBody Document document) {
        checkEligibility(document, "delete");

        documentRepository.deleteById(document.getId());
    }

    private void checkEligibility(Document document, String operation) {
        if(document.getEmployee() == null){
            throw new BusinessException("Employee not mentioned");
        }

        Employee employee = document.getEmployee();

        Employee fetchedEmp = employeeRepository.findById(employee.getCin()).orElseThrow(
                () -> new RuntimeException("There is no employee with cin (" + employee.getCin() + ")")
        );

        if(fetchedEmp.getUser() == null
                || !fetchedEmp.getUser().getUsername().equals(
                authenticationService.getAuthenticatedUser().getUsername())){
            throw new BusinessException("You are not allowed to " + operation + " a document to the Employee with cin (" + employee.getCin() + "), since it wasn't added by you");
        }
    }

    @GetMapping()
    public Page<Document> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "employee", required = false) String employee,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return documentRepository.search(name, category, employee, pageable);
    }

    @GetMapping("/count")
    public Count count() {
        return new Count(documentRepository.count());
    }
}
