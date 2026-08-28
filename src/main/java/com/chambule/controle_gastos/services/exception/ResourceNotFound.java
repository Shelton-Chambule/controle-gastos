package com.chambule.controle_gastos.services.exception;

public class ResourceNotFound extends RuntimeException {

    public ResourceNotFound(Object id) {
        super("Resource not fund "+ id);
    }
}
