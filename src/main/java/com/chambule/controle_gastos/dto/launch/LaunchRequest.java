package com.chambule.controle_gastos.dto.launch;
import com.chambule.controle_gastos.entities.enums.LaunchType;
import com.chambule.controle_gastos.entities.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LaunchRequest {

    @NotBlank(message = "Required field")
    @JsonProperty(required = true)
    @Size(min = 100, max = 250, message = "Description must have between 3 and 150 characters")
    private String description;

    @NotNull(message = "Required field")
    @Positive(message = "Value must be positive")
    @JsonProperty(required = true)
    private BigDecimal value;

    @NotNull(message = "Required field")
    @JsonProperty(required = true)
    private LaunchType type;

    @NotNull(message = "Required field")
    @JsonProperty(required = true)
    @PastOrPresent(message = "The date cannot be in the future")
    private LocalDate transactionDate;

    @NotNull(message = "Required field")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Required field")
    @JsonProperty(required = true)
    private Long categoryId;

}
