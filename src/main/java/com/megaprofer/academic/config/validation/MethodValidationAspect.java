package com.megaprofer.academic.config.validation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.groups.Default;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Aspect
public class MethodValidationAspect {

    @Autowired
    private Validator validator;

    @Around("execution(* ec.com.alquimiasoft.megapos.*.service..*.*(..))")
    public Object validateServiceMethodParameters(ProceedingJoinPoint pjp) throws Throwable {
        ExecutableValidator executableValidator = validator.forExecutables();
        Method method = ((MethodSignature) pjp.getStaticPart().getSignature()).getMethod();
        Object[] args = pjp.getArgs();
        Class<?>[] groups = new Class<?>[]{Default.class};
        if (method.isAnnotationPresent(Validated.class)) {
            groups = method.getAnnotation(Validated.class).value();
        }
        Set<ConstraintViolation<Object>> constraintViolations =  executableValidator
                .validateParameters(pjp.getThis(), method, args, groups);
        if(constraintViolations.isEmpty()){
            return pjp.proceed();
        }else{
            List<String> validationsMessages = new ArrayList<>();
            for(ConstraintViolation<Object> constraintViolation : constraintViolations){
                validationsMessages.add(String.format("%s : {%s} ", constraintViolation.getPropertyPath(),
                        constraintViolation.getMessage()));
            }
            throw new ValidationException(String.format("Invalid parameters %s", validationsMessages));

        }
    }


}
