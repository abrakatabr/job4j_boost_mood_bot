package ru.job4j.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class MoodService extends BeanNameAware {

    @PostConstruct
    public void init() {
        System.out.println("Bean MoodService created");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean MoodService will be destroyed");
    }
}
