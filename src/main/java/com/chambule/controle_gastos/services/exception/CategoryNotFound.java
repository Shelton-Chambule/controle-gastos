package com.chambule.controle_gastos.services.exception;

public class CategoryNotFound extends RuntimeException {
    public CategoryNotFound(String message) {
        super("Category not found");
    }
}
