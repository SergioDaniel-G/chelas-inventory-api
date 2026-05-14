package com.micheladas.chelas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * CUSTOM EXCEPTION THROWN WHEN A REQUESTED RESOURCE (ENTITY) CANNOT BE FOUND
 * IN THE DATABASE. AUTOMATICALLY MAPS TO AN HTTP 404 NOT FOUND STATUS.
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("No se encontró %s con %s: '%s'", resourceName, fieldName, fieldValue));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
