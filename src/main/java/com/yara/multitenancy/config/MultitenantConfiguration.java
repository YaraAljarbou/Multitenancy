package com.yara.multitenancy.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultitenantConfiguration {

    private final MultitenantDataSource multitenantDataSource;

    public MultitenantConfiguration(MultitenantDataSource multitenantDataSource) {
        this.multitenantDataSource = multitenantDataSource;
    }

    @Bean
    public DataSource dataSource() {
        TenantRoutingDataSource customDataSource = new TenantRoutingDataSource();
        customDataSource.setDefaultTargetDataSource(multitenantDataSource.getDatasources().get("tenant_1"));
        customDataSource.setTargetDataSources(
                multitenantDataSource.getDatasources());
        return customDataSource;
    }
}
