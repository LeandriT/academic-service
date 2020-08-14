package com.megaprofer.academic.exception;

import org.springframework.http.HttpStatus;

public class MegaPosException extends RuntimeException {
    private HttpStatus httpStatus;

    public MegaPosException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ExceptionResponse getExceptionResponse() {
        return ExceptionResponse.builder()
                .message(getLocalizedMessage())
                .build();
    }
}
