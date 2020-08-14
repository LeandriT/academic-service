package com.megaprofer.academic.exception.handler;

import com.megaprofer.academic.config.log.RequestScopeAttributes;
import com.megaprofer.academic.exception.ExceptionResponse;
import com.megaprofer.academic.exception.MegaPosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GenericExceptionHandler {

    public static final String RESPONSE_STATUS_KEY = "responseStatusKey";
    public static final String REQUEST_ID = "requestId";

    @Autowired
    private RequestScopeAttributes requestScopeAttributes;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> genericExceptionHandler(Exception exception) {
        HttpStatus internalServerError;
        if (exception instanceof AccessDeniedException) {
            internalServerError = HttpStatus.FORBIDDEN;
        } else {
            internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message("Ocurrió un error inesperado")
                .build();

        ResponseEntity<ExceptionResponse> response = new ResponseEntity<>(
                exceptionResponse,
                internalServerError
        );


        return response;
    }

    @ExceptionHandler(MegaPosException.class)
    public ResponseEntity<ExceptionResponse> customExceptionHandler(MegaPosException exception) {
        ResponseEntity<ExceptionResponse> response = new ResponseEntity<>(exception.getExceptionResponse(), exception.getHttpStatus());
        return response;
    }

}
