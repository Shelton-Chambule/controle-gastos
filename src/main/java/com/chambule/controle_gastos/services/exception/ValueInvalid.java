package com.chambule.controle_gastos.services.exception;
public class ValueInvalid extends RuntimeException {

    public ValueInvalid(String message) {
        super("Value invalid");
    }
}
