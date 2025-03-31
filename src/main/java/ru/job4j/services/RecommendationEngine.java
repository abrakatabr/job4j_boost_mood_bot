package ru.job4j.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class RecommendationEngine {

    @PostConstruct
    public void init() {
        System.out.println("Bean RecommendationEngine created");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean RecommendationEngine will be destroyed");
    }
}
