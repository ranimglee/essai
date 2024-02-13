package tn.esprit.examen.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;



@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Around("execution(void tn.esprit.examen.Services.*.*(..))")

    public Object profile(ProceedingJoinPoint pjp) throws Throwable{
       long start = System.currentTimeMillis();
       Object obj =pjp.proceed();
       long elapsedTime = System.currentTimeMillis() - start;
       log.info("Method execution time:"+elapsedTime+"milliseconds.");
       return obj;

    }

}
