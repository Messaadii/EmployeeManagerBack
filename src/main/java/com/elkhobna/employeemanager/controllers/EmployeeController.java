package com.elkhobna.employeemanager.controllers;

import com.elkhobna.employeemanager.entities.Company;
import com.elkhobna.employeemanager.entities.Employee;
import com.elkhobna.employeemanager.repositories.EmployeeRepository;
import com.elkhobna.employeemanager.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/employee")
@RestController
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final AuthenticationService authenticationService;

    public EmployeeController(EmployeeRepository employeeRepository, AuthenticationService authenticationService) {
        this.employeeRepository = employeeRepository;
        this.authenticationService = authenticationService;
    }

    @PostMapping()
    public ResponseEntity<Employee> save(@Valid @RequestBody Employee employee) {
        Optional<Employee> fetchedEmployee = employeeRepository.findById(employee.getCin());
        if(fetchedEmployee.isPresent()){
            throw new RuntimeException("There is an existing employee with the cin (" + employee.getCin() + ")");
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
        employeeRepository.delete(fetchedEmp);
    }

    @GetMapping
    public Iterable<Employee> findAll(){
        return employeeRepository.findAll();
    }

    public Employee checkEligibility(Employee employee, String operation){
        Employee fetchedEmp = employeeRepository.findById(employee.getCin()).orElseThrow(
                () -> new RuntimeException("There is no employee with cin (" + employee.getCin() + ")")
        );
        if(fetchedEmp.getUser() == null
                || !fetchedEmp.getUser().getUsername().equals(
                authenticationService.getAuthenticatedUser().getUsername())){
            throw new RuntimeException("You are not allowed to " + operation + " the Employee with cin (" + employee.getCin() + "), since it wasn't added by you");
        }
        return fetchedEmp;
    }
}
