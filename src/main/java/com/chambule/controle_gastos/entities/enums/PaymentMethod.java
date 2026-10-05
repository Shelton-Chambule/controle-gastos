package com.chambule.controle_gastos.entities.enums;
public enum PaymentMethod {

    CASH("cash"),
    PIX("pix"),
    DEBIT_CARD("debit card"),
    CREDIT_CARD("credit card"),
    BANK_TRANSFER("bank transfer"),
    BANK_SLIP("bank slip"),
    OTHER("other");

    private String  code;

    PaymentMethod(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
