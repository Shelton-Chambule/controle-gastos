package com.chambule.controle_gastos.entities.enums;
public enum PaymentMethod {

    CASH(1),
    PIX(2),
    DEBIT_CARD(3),
    CREDIT_CARD(4),
    BANK_TRANSFER(5),
    BANK_SLIP(6),
    OTHER(7);

    private int code;

    PaymentMethod(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PaymentMethod paymentMethod(int code) {
        for (PaymentMethod payment : PaymentMethod.values()) {
            if (payment.getCode() == code) {
                return payment;
            }
        }
        throw new IllegalArgumentException("invalid code!");
    }
}
