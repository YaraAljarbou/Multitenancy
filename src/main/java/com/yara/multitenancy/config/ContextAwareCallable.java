package com.yara.multitenancy.config;

import com.yara.multitenancy.Service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.Callable;

public class ContextAwareCallable<T> implements Callable<T> {
    private final Callable<T> task;
    private final RequestAttributes context;
    private String tenant;
    private final JwtService jwtService;

    ContextAwareCallable(Callable<T> task, RequestAttributes context, JwtService jwtService) {
        this.task = task;
        this.context = context;
        this.jwtService = jwtService;
    }

    @Override
    public T call() throws Exception {
        if (context != null) {
            RequestContextHolder.setRequestAttributes(context, true);
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                tenant = jwtService.extractUsername(token);
            }
        }


            try {
                TenantContext.setCurrentTenant(tenant);
                return task.call();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        }
    }
