package com.chambule.controle_gastos.services.exception;

public class DataBase extends RuntimeException {
    public DataBase(String message) {
        super("Data base, violet constraint");
    }
}
