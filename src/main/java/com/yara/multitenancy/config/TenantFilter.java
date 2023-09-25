package com.yara.multitenancy.config;

//import com.yara.multitenancy.security.AuthenticationService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class TenantFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        // Getting Tenant from Header
        HttpServletRequest req = (HttpServletRequest) request;
        String tenant = req.getHeader("X-TenantID");

        // Getting Tenant from JWT
        // String tenant = AuthenticationService.getTenant((HttpServletRequest) request);

        TenantContext.setCurrentTenant(tenant);

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clearCurrentTenant();
        }
    }
}
