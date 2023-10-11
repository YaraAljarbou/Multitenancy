package com.yara.multitenancy.config;

//import com.yara.multitenancy.security.AuthenticationService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import java.io.IOException;

@Component
@Order(1)
public class TenantFilter implements Filter, AsyncHandlerInterceptor {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        // Getting Tenant from Header
        if(TenantContext.getCurrentTenant() == null) {
            HttpServletRequest req = (HttpServletRequest) request;
            String tenant = req.getHeader("X-TenantID");
            TenantContext.setCurrentTenant(tenant);
        }

        // Getting Tenant from JWT
//         String tenant = AuthenticationService.getTenant((HttpServletRequest) request);

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clearCurrentTenant();
        }
    }
}
