package com.elkhobna.employeemanager.controllers;

import com.elkhobna.employeemanager.entities.Document;
import com.elkhobna.employeemanager.entities.Employee;
import com.elkhobna.employeemanager.repositories.DocumentRepository;
import com.elkhobna.employeemanager.repositories.EmployeeRepository;
import com.elkhobna.employeemanager.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/document")
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

        documentRepository.delete(document);
    }

    @GetMapping
    public Iterable<Document> findAll(){
        return documentRepository.findAll();
    }

    private void checkEligibility(Document document, String operation) {
        if(document.getEmployee() == null){
            throw new RuntimeException("Employee not mentioned");
        }

        Employee employee = document.getEmployee();

        Employee fetchedEmp = employeeRepository.findById(employee.getCin()).orElseThrow(
                () -> new RuntimeException("There is no employee with cin (" + employee.getCin() + ")")
        );

        if(fetchedEmp.getUser() == null
                || !fetchedEmp.getUser().getUsername().equals(
                authenticationService.getAuthenticatedUser().getUsername())){
            throw new RuntimeException("You are not allowed to " + operation + " a document to the Employee with cin (" + employee.getCin() + "), since it wasn't added by you");
        }
    }
}
