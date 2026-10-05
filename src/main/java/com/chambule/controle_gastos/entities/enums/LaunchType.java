package com.chambule.controle_gastos.entities.enums;
public enum LaunchType {

    INCOME("income"),
    EXPENSE("expense");

    private String code;

    LaunchType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
