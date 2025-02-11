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
public class CommentControllerAspect {

    @Pointcut("execution(* ru.clevertec.adapter.input.web.comment.CommentController.*(..))")
    public void commentControllerMethods() {}

    @Before("commentControllerMethods()")
    public void beforeCommentControllerMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("CommentController.{}: invoke method with arguments: {}", methodName, args);
    }

    @AfterReturning(pointcut = "commentControllerMethods()", returning = "result")
    public void afterReturningCommentControllerMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.debug("CommentController.{}: finish method with result: {}", methodName, result);
    }

    @AfterThrowing(pointcut = "commentControllerMethods()", throwing = "exception")
    public void afterThrowingCommentControllerMethod(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.error("CommentController.{}: throw {}", methodName, exception.getMessage(), exception);
    }
}
