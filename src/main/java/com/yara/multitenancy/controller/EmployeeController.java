package com.yara.multitenancy.controller;

import com.yara.multitenancy.Service.EmployeeService;
import com.yara.multitenancy.config.TenantContext;
import com.yara.multitenancy.repository.EmployeeRepository;
import com.yara.multitenancy.entity.Employee;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PostMapping(path = "/employee")
    public ResponseEntity<?> createEmployee() {
        Employee newEmployee = new Employee();
        newEmployee.setName("Yara");
        employeeRepository.save(newEmployee);
        return ResponseEntity.ok(newEmployee);
    }
    @GetMapping(path = "/employee")
    public CompletableFuture<List<Employee>> getEmployees(HttpServletRequest req)
    {
        String tenant = req.getHeader("X-TenantID");
        System.out.println("tenant --> " + tenant);
        List<Employee> employees = employeeRepository.findAll();
        System.out.println(employees.size());
        return CompletableFuture.completedFuture(employees);

//    return CompletableFuture.supplyAsync(() -> {
//        String tenant = req.getHeader("X-TenantID");
//        System.out.println("tenant --> " +tenant);
//        TenantContext.setCurrentTenant(tenant);
//        List<Employee> employees = employeeRepository.findAll();
//        System.out.println(employees.size());
//        return employees;
//          });
    }



}
