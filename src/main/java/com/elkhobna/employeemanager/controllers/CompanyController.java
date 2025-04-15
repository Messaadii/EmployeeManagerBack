package com.elkhobna.employeemanager.controllers;

import com.elkhobna.employeemanager.dtos.CompanyByCount;
import com.elkhobna.employeemanager.dtos.Count;
import com.elkhobna.employeemanager.dtos.CountByDate;
import com.elkhobna.employeemanager.dtos.SimpleCompany;
import com.elkhobna.employeemanager.entities.Company;
import com.elkhobna.employeemanager.exceptions.BusinessException;
import com.elkhobna.employeemanager.repositories.CompanyRepository;
import com.elkhobna.employeemanager.repositories.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RequestMapping("/company")
@RestController
public class CompanyController {

    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    public CompanyController(CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping()
    public ResponseEntity<Company> save(@Valid @RequestBody Company company) {
        Optional<Company> fetchedCompany = companyRepository.findById(company.getName());
        if(fetchedCompany.isPresent()){
            throw new BusinessException("There is an existing company with the name (" + company.getName() + ")");
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
        Long employees = employeeRepository.countByCompany(company.getName());
        if(employees > 0){
            throw new BusinessException("This company has " + employees +
                    " employee" + (employees == 1 ? "" : "s") + ", please delete " + (employees == 1 ? "it" : "them") + " first.");
        }
        companyRepository.deleteById(company.getName());
    }

    @GetMapping()
    public Page<Company> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return companyRepository.search(name, description, pageable);
    }

    @GetMapping("/simple-find")
    public List<SimpleCompany> simpleFind() {
        return companyRepository.simpleFind();
    }

    @GetMapping("/count-employees-by-company-and-joining-date")
    public Collection<CountByDate> countEmployeesByCompanyAndJoiningDate() {
        Pageable pageable = PageRequest.of(0, 10);

        Map<LocalDate, CountByDate> counts = new LinkedHashMap<>();
        for(String companyName : companyRepository.simpleFind().stream().map(SimpleCompany::getName).toList()) {
            List<Object[]> list = companyRepository.countEmployeesByCompanyAndJoiningDate(companyName, pageable);
            flashCounts(companyName, counts, list);
        }

        return counts.values();
    }

    private void flashCounts(String companyName, Map<LocalDate, CountByDate> counts, List<Object[]> list) {
        Long cumul = 0L;

        for(int i = list.size() - 1 ; i >= 0; i--) {
            Object[] obj = list.get(i);
            LocalDate joiningDate = (LocalDate) obj[0];
            if(!counts.containsKey(joiningDate)) {
                counts.put(joiningDate, new CountByDate(joiningDate));
            }

            Long count = (Long) obj[1];
            cumul += count;
            counts.get(joiningDate).addCompany(new CompanyByCount(companyName, cumul));
        }
    }

    @GetMapping("/count")
    public Count count() {
        return new Count(companyRepository.count());
    }
}
