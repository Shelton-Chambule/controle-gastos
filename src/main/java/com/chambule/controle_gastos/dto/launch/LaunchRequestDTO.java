package com.chambule.controle_gastos.dto.launch;

import com.chambule.controle_gastos.entities.enums.LaunchType;
import com.chambule.controle_gastos.entities.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LaunchRequestDTO {

    @NotBlank(message = "Required field")
    @Size(min = 3, max = 150, message = "Description must have between 3 and 150 characters")
    private String description;

    @NotNull(message = "Required field")
    @Positive(message = "Value must be positive")
    private BigDecimal value;

    @NotNull(message = "Required field")
    private LaunchType type;

    @NotNull(message = "Required field")
    @PastOrPresent(message = "The date cannot be in the future")
    private LocalDate transactionDate;

    @NotNull(message = "Required field")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Required field")
    private Long categoryId;

    public LaunchRequestDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LaunchType getType() {
        return type;
    }

    public void setType(LaunchType type) {
        this.type = type;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
