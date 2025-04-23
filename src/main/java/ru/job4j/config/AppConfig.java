package ru.job4j.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Component
@EnableAspectJAutoProxy
public class AppConfig {

    @Value("${telegram.bot.name}")
    private String appName;
}
