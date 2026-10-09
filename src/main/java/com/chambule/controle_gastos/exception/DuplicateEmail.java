package com.chambule.controle_gastos.exception;

public class DuplicateEmail extends RuntimeException {
    public DuplicateEmail(String message) {
        super(" Esse email ja se encontra cadastrado!");
    }
}
