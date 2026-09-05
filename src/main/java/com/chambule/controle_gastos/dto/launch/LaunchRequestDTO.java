package com.chambule.controle_gastos.dto.launch;
import com.chambule.controle_gastos.entities.Launch;
import com.chambule.controle_gastos.entities.enums.LaunchType;
import com.chambule.controle_gastos.entities.enums.PaymentMethod;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
public class LaunchRequestDTO {

    @NotBlank(message = "Required field")
    @Size(min = 20, max = 50, message = "must has in the  maxim 50  characters")
    private String description;

    @NotNull(message = "Required field")
    @Positive(message = "valor deve  ser  positivo")
    private BigDecimal value;

    @NotNull(message = "Required field")
    private Integer type;

    @NotNull(message = "Required field")
    @PastOrPresent(message = "The date cannot be in the future")
    private LocalDate transactionDate;

    @NotNull(message = "Required field")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Required field")
    private Long categoryId;

    @NotNull(message = "Required field")
    private Long userId;

    public LaunchRequestDTO(){}

    public LaunchRequestDTO(Launch launch) {
        this.description = launch.getDescription();
        this.value = launch.getValue();
        setType(launch.getLaunchType());
        this.transactionDate = launch.getTransactionDate();
        this.paymentMethod = launch.getPaymentMethod();
        this.categoryId  = launch.getCategory().getId();
        this.userId = launch.getId();
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

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LaunchType getType() {
        return LaunchType.launch(type);
    }

    public void setType(LaunchType type) {
        this.type = type.getCode();
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
