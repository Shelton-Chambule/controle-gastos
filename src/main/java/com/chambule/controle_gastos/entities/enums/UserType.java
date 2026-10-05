package com.chambule.controle_gastos.entities.enums;
public enum UserType {

    ADMIN("admin"),
    USER("user");

    private String code;

    UserType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
