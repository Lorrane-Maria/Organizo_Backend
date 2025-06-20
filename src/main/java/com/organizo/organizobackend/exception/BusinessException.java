package com.organizo.organizobackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada para erros gerais de regra de negócio.
 * Mapeada para o status HTTP 400 Bad Request por padrão, mas pode ser sobrescrita no handler.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST) // Pode ser ajustado no handler global
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}