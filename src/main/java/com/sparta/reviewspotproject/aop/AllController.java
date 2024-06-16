package com.sparta.reviewspotproject.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j(topic = "AllController")
@Aspect
public class AllController {

    @Pointcut("execution(* com.sparta.reviewspotproject.controller.*.*(..))")
    private void forAllController() {
    }

    @Around("forAllController()")
    public Object excute(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = null;

        try {
            request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest();
            return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            log.info("Request: {} {}:", request.getMethod(), request.getRequestURL());
        }
    }
}
