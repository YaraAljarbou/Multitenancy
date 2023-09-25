package com.yara.multitenancy.config;

public class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static void setCurrentTenant(String tenant) {
        CURRENT_TENANT.set(tenant);
    }
    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }
    public static void clearCurrentTenant() { CURRENT_TENANT.remove(); }
}
