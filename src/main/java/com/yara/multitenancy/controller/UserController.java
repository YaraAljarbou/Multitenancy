package com.yara.multitenancy.controller;

import com.yara.multitenancy.Service.JwtService;
import com.yara.multitenancy.Service.UserInfoService;
import com.yara.multitenancy.config.TenantContext;
import com.yara.multitenancy.entity.AuthRequest;
import com.yara.multitenancy.entity.Employee;
import com.yara.multitenancy.entity.MutableHttpServletRequest;
import com.yara.multitenancy.entity.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserInfoService service;
    private final EmployeeController employeeController;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public UserController(UserInfoService service, EmployeeController employeeController, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.service = service;
        this.employeeController = employeeController;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public CompletableFuture<List<Employee>> userProfile() {
        return employeeController.getEmployees();
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

}
