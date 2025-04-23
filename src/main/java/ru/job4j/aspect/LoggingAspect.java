package ru.job4j.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

@Aspect
@Component
public class LoggingAspect {
    private Logger logger = LoggerFactory.getLogger(LoggingAspect.class.getName());

    @Pointcut("execution(* ru.job4j.services.*.*(..))")
    private void serviceLayer() { }

    @Before("serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        StringJoiner args = new StringJoiner(" ");
        for (Object arg : joinPoint.getArgs()) {
            args.add(arg.toString());
        }
        logger.info("Метод: " + joinPoint.getSignature().getName() + ". Аргументы: "
        + args.toString());
    }
}
