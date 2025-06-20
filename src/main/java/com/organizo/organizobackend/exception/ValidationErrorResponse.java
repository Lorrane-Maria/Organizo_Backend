package com.organizo.organizobackend.exception;

import java.util.List;

/**
 * Resposta de erro para validações JSR-303.
 */
public class ValidationErrorResponse {
    private int status;
    private String message;
    private long timestamp;
    private List<ValidationError> errors;

    public ValidationErrorResponse(int status, String message, long timestamp, List<ValidationError> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.errors = errors;
    }
    // getters e setters
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public List<ValidationError> getErrors() {
        return errors;
    }
    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }
}
