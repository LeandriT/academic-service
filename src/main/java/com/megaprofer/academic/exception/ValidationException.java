package com.megaprofer.academic.exception;

import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationException extends MegaPosException {

    public ValidationException(String exString) {
        super(HttpStatus.PRECONDITION_FAILED, exString);
    }

    public ValidationException(String exString, Set<ConstraintViolation<Object>> constraintViolations) {
        super(HttpStatus.PRECONDITION_FAILED, createMessage(exString, constraintViolations));
    }

    private static String createMessage(String exString, Set<ConstraintViolation<Object>> constraintViolations) {
        if (constraintViolations == null)
            return "";

        return String.format("Datos incorrectos: %s",
                constraintViolations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(", "))
        );
    }

}
