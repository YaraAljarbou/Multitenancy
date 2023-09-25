package com.yara.multitenancy.controller;

import com.yara.multitenancy.entity.Employee;
import com.yara.multitenancy.repository.EmployeeRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping(path = "/employee")
    public ResponseEntity<?> createEmployee(HttpServletRequest req) {
        Employee newEmployee = new Employee();
        newEmployee.setName("Yara");
        employeeRepository.save(newEmployee);
        return ResponseEntity.ok(newEmployee);
    }
}
