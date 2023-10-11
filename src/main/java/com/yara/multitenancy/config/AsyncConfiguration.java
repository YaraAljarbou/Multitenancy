package com.yara.multitenancy.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration {


    @Bean(name = "taskExecutor")
    public DelegatingSecurityContextAsyncTaskExecutor taskExecutor(ThreadPoolTaskExecutor delegate) {
        return new DelegatingSecurityContextAsyncTaskExecutor(delegate);
    }
    @Bean(name = "contextAwareThreadPoolExecutor")
    public ThreadPoolTaskExecutor contextAwareThreadPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ContextAwareThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(12);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("my-context-aware-thread");
        executor.initialize();
        return executor;
    }

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor()  {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setAwaitTerminationMillis(300);
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(12);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}