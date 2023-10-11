package com.yara.multitenancy.config;

import com.yara.multitenancy.Service.JwtService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ContextAwareThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {


    @Override
    public void execute(Runnable task) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        super.execute(new ContextAwareRunnable(task,requestAttributes, new JwtService()));
    }



    @Override
    public <T> Future<T> submit(Callable<T> task) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();

        return super.submit(new ContextAwareCallable<>(task, requestAttributes, new JwtService()));
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();

        return super.submitListenable(new ContextAwareCallable<>(task, requestAttributes, new JwtService()));
    }
}