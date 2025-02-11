package ru.clevertec.adapter.input.web.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class NewsControllerAspect {

    @Pointcut("execution(* ru.clevertec.adapter.input.web.news.NewsController*(..))")
    public void newsControllerMethods() {}

    @Before("newsControllerMethods()")
    public void beforeNewsControllerMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("NewsController.{}: invoke method with arguments: {}", methodName, args);
    }

    @AfterReturning(pointcut = "newsControllerMethods()", returning = "result")
    public void afterReturningNewsControllerMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.debug("NewsController.{}: finish method with result: {}", methodName, result);
    }

    @AfterThrowing(pointcut = "newsControllerMethods()", throwing = "exception")
    public void afterThrowingNewsControllerMethod(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.error("NewsController.{}: throw {}", methodName, exception.getMessage(), exception);
    }
}
