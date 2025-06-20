package com.organizo.organizobackend.exception;

/**
 * Representa um erro de validação de um campo específico.
 */
public class ValidationError {
    private String field;
    private String error;

    public ValidationError(String field, String error) {
        this.field = field;
        this.error = error;
    }
    // getters e setters
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
}
