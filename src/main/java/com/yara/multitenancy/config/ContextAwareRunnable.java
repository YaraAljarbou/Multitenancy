package com.yara.multitenancy.config;

import com.yara.multitenancy.Service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ContextAwareRunnable implements Runnable {

    final Runnable task;
    private final RequestAttributes context;
    private String tenant;
    private final JwtService jwtService;

    ContextAwareRunnable(Runnable task, RequestAttributes context, JwtService jwtService) {
        this.task = task;
        this.context = context;
        this.jwtService = jwtService;
    }

    @Override
    public void run() {
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
            task.run();
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }
}