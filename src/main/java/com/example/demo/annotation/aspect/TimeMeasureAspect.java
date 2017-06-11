package com.example.demo.annotation.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class TimeMeasureAspect {
	
	@Around("@annotation(com.example.demo.annotation.Timed) && execution(* *(..))")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnObject = null;
        final long startTime = System.currentTimeMillis();
         
        try {
            returnObject = joinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        }
        finally {
           final long elepsedTime = System.currentTimeMillis() - startTime;
           log.info("method: {}, elepsed time: {}", joinPoint.getSignature().toShortString(),elepsedTime);
        }
        return returnObject;
    }
}
