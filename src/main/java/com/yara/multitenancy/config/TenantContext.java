package com.yara.multitenancy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();
    
    public static void setCurrentTenant(String tenant) {
        if(tenant == null) return;
        CURRENT_TENANT.set(tenant);
    }
    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }
    public static void clearCurrentTenant() { CURRENT_TENANT.remove(); }
}
