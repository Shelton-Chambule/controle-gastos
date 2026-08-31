package com.chambule.controle_gastos.dto.launch;
import com.chambule.controle_gastos.entities.Launch;
import com.chambule.controle_gastos.entities.enums.LaunchType;
import com.chambule.controle_gastos.entities.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDate;
public class LaunchResponseDTO {

    private Long id;
    private String description;
    private BigDecimal value;
    private LaunchType type;
    private LocalDate transactionDate;
    private PaymentMethod paymentMethod;
    private Long categoryId;
    private Long userId;

    public LaunchResponseDTO(){}

    public LaunchResponseDTO(Launch launch) {
         this.id = launch.getId();
        this.description = launch.getDescription();
        this.value = launch.getValue();
        this.type = launch.getLaunchType();
        this.transactionDate = launch.getTransactionDate();
        this.paymentMethod = launch.getPaymentMethod();
        this.categoryId = launch.getCategory().getId();
        this.userId = launch.getUser().getId();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return type;
    }

    public void setType(LaunchType type) {
        this.type = type;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
