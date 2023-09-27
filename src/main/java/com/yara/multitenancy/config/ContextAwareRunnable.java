package com.yara.multitenancy.config;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class ContextAwareRunnable implements Runnable {

    final Runnable task;
    private final RequestAttributes context;

    ContextAwareRunnable(Runnable task, RequestAttributes context) {
        this.task = task;
        this.context = context;
    }

    @Override
    public void run() {
        if (context != null) {
            RequestContextHolder.setRequestAttributes(context, true);
        }


        try {
            task.run();
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }
}