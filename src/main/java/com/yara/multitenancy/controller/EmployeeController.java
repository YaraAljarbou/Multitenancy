package com.yara.multitenancy.controller;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import com.yara.multitenancy.repository.EmployeeRepository;
import com.yara.multitenancy.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Async("taskExecutor")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public CompletableFuture<List<Employee>> getEmployees() {
        return CompletableFuture.completedFuture(employeeRepository.findAll());
    }
    @Async
    @GetMapping(path = "/employee-async")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public CompletableFuture<List<Employee>> getEmployeesAsync()
    {
        CompletableFuture<List<Employee>> future = new CompletableFuture<>();
        // Defining the Timeout Period
        future.orTimeout(1, TimeUnit.SECONDS)
                .exceptionally(ex -> {
                    CompletableFuture<List<Employee>> failedFuture = new CompletableFuture<>();
                    if (ex instanceof TimeoutException) {
                        future.cancel(true); // Cancel the future if it times out
                        System.out.println("Timeout, the task will be cancelled");
                    }
                    return null;
                });

        // Perform asynchronous getAll operation
        try {
            // Simulate a long-running task
//            Thread.sleep(3000);

            List<Employee> employees = employeeRepository.findAll();
            future.complete(employees);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;
    }


}
