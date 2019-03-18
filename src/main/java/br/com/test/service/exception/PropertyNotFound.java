package br.com.test.service.exception;

import org.springframework.http.HttpStatus;

public class PropertyNotFound extends BusinessException {
    public PropertyNotFound(String errorCode, String propertyName, String typeSimpleName) {
        super(errorCode, HttpStatus.NOT_FOUND, propertyName, typeSimpleName);
    }
}
