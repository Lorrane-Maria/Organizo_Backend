package com.organizo.organizobackend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * Exceção lançada quando ocorrem erros de validação de dados (ex: DTOs).
 * Contém um mapa de erros (campo -> mensagem).
 * Mapeada para o status HTTP 400 Bad Request.
 */
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public ValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

    public ValidationException(Map<String, String> errors) {
        super("Erro de validação");
        this.errors = errors;
    }
}