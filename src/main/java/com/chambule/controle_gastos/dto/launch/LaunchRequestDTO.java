package com.chambule.controle_gastos.dto.launch;
import com.chambule.controle_gastos.entities.Launch;
import com.chambule.controle_gastos.entities.enums.LaunchType;
import com.chambule.controle_gastos.entities.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public class LaunchRequestDTO {

    @NotBlank(message = "Campo obrigatório")
    @Size(min = 20, max = 50, message = "deve  ter  no máximo 50 caracteres")
    private String description;

    @NotNull(message = "Campo obrigatório")
    @Positive(message = "valor deve  ser  positivo")
    private BigDecimal value;

    @NotNull(message = "Campo obrigatório")
    private LaunchType type;

    @NotNull(message = "Campo obrigatório")
    private LocalDate transactionDate;

    @NotNull(message = "Campo obrigatório")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Campo obrigatório")
    private Long idCategory;

    public LaunchRequestDTO(){}

    public LaunchRequestDTO(Launch launch) {
        this.description = launch.getDescription();
        this.value = launch.getValue();
        this.type = launch.getLaunchType();
        this.transactionDate = launch.getTransactionDate();
        this.paymentMethod = launch.getPaymentMethod();
        this.idCategory = launch.getCategory().getIdCategory();
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

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }
}
