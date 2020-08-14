package com.megaprofer.academic.config.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class ServiceLogger {

    //    private Logbook logbook = Logbook.instance(ServiceLogger.class);
    @Value("${kafka.listener.consumerGroup}")
    private String group;

    @Around("execution(* com.megaprofer.academic.*.service.impl..*.*(..))")
    public Object serviceInterceptor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object output = proceedingJoinPoint.proceed();
        stopWatch.stop();
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        /*logbook.type("service")
                .duration(stopWatch.getLastTaskTimeMillis())
                .service(proceedingJoinPoint.getTarget().getClass().getName())
                .javaMethod(signature.getMethod())
                .add("group", group)
                .info();*/
        return output;
    }

    @AfterThrowing(pointcut = "execution(* com.megaprofer.academic.*.service.impl..*.*(..))", throwing = "ex")
    public void serviceExceptionsInterceptor(JoinPoint jp, Throwable ex) {
        /*logbook.type("kafkaListenerException")
                .javaMethod(jp.getSignature().getName())
                .service(jp.getTarget().getClass().getName())
                .exception(ex.getClass().getName())
                .exceptionWithStackTrace(ex)
                .message(ex.getMessage())
                .add("group", group)
                .error();*/
    }

    @Around("execution(* com.megaprofer.academic.*.listener..*.*(..))")
    public Object listenerInterceptor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object output = proceedingJoinPoint.proceed();
        stopWatch.stop();
        /*logbook.type("kafkaListener")
                .duration(stopWatch.getLastTaskTimeMillis())
                .add("group", group)
                .info();*/
        return output;
    }

    @AfterThrowing(pointcut = "execution(* com.megaprofer.academic.*.listener..*.*(..))", throwing = "ex")
    public void listenerExceptionsInterceptor(JoinPoint jp, Throwable ex) {
        /*logbook.type("kafkaListener")
                .javaMethod(jp.getSignature().getName())
                .service(jp.getTarget().getClass().getName())
                .exception(ex.getClass().getName())
                .message(ex.getMessage())
                .add("group", group)
                .error();*/
    }

}
