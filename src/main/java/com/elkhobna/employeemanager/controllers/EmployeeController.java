package com.elkhobna.employeemanager.controllers;

import com.elkhobna.employeemanager.dtos.Count;
import com.elkhobna.employeemanager.dtos.SimpleCategory;
import com.elkhobna.employeemanager.dtos.SimpleEmployee;
import com.elkhobna.employeemanager.entities.Employee;
import com.elkhobna.employeemanager.exceptions.BusinessException;
import com.elkhobna.employeemanager.repositories.DocumentRepository;
import com.elkhobna.employeemanager.repositories.EmployeeRepository;
import com.elkhobna.employeemanager.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequestMapping("/employee")
@RestController
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final AuthenticationService authenticationService;
    private final DocumentRepository documentRepository;

    public EmployeeController(EmployeeRepository employeeRepository,
                              AuthenticationService authenticationService,
                              DocumentRepository documentRepository) {
        this.employeeRepository = employeeRepository;
        this.authenticationService = authenticationService;
        this.documentRepository = documentRepository;
    }

    @PostMapping()
    public ResponseEntity<Employee> save(@Valid @RequestBody Employee employee) {
        Optional<Employee> fetchedEmployee = employeeRepository.findById(employee.getCin());
        if(fetchedEmployee.isPresent()){
            throw new BusinessException("There is an existing employee with the cin (" + employee.getCin() + ")");
        }

        Optional<Employee> fetchedEmployeeById = employeeRepository.findByID(employee.getId());
        if(fetchedEmployeeById.isPresent()){
            throw new BusinessException("There is an existing employee with the id (" + employee.getId() + ")");
        }

        employee.setUser(authenticationService.getAuthenticatedUser());
        return ResponseEntity.ok(
                employeeRepository.save(employee)
        );
    }

    @PutMapping()
    public ResponseEntity<Employee> update(@Valid @RequestBody Employee employee) {
        Employee fetchedEmp = checkEligibility(employee, "update");
        employee.setUser(authenticationService.getAuthenticatedUser());
        return ResponseEntity.ok(
                employeeRepository.save(employee)
        );
    }

    @DeleteMapping()
    public void delete(@RequestBody Employee employee) {
        Employee fetchedEmp = checkEligibility(employee, "delete");

        Long documents = documentRepository.countByEmployee(employee.getCin());

        if(documents > 0){
            throw new BusinessException("This employee has " + documents +
                    " document" + (documents == 1 ? "" : "s") + ". Please remove " + (documents == 1 ? "it" : "them") + " first.");
        }

        employeeRepository.deleteById(employee.getCin());
    }

    @GetMapping()
    public Page<Employee> search(
            @RequestParam(value = "cin", required = false) String cin,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "familyName", required = false) String familyName,
            @RequestParam(value = "company", required = false) String company,
            @RequestParam(value = "joiningDate", required = false) @DateTimeFormat(pattern = "yyyy-dd-MM") LocalDate joiningDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.search(cin, id, name, familyName, company, joiningDate, pageable);
    }

    public Employee checkEligibility(Employee employee, String operation){
        Employee fetchedEmp = employeeRepository.findById(employee.getCin()).orElseThrow(
                () -> new RuntimeException("There is no employee with cin (" + employee.getCin() + ")")
        );
        if(fetchedEmp.getUser() == null
                || !fetchedEmp.getUser().getUsername().equals(
                authenticationService.getAuthenticatedUser().getUsername())){
            throw new BusinessException("You are not allowed to " + operation + " the Employee with cin (" + employee.getCin() + "), since it wasn't added by you");
        }
        return fetchedEmp;
    }

    @GetMapping("/simple-find")
    public List<SimpleEmployee> simpleFind() {
        return employeeRepository.simpleFind();
    }

    @GetMapping("/count")
    public Count count() {
        return new Count(employeeRepository.count());
    }
}
