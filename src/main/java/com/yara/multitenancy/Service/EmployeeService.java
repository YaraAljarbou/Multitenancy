package com.yara.multitenancy.Service;

import com.yara.multitenancy.entity.Employee;
import com.yara.multitenancy.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmployeeService {

private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public CompletableFuture<List<Employee>> getEmployees()
    {
    return CompletableFuture.supplyAsync(() -> {
        log.info("getEmployees starts");
        List<Employee> employees = employeeRepository.findAll();
        log.info("employeeNameData completed");
        return employees;
          });
    }
}
