package com.chambule.controle_gastos.dto.launch;
import com.chambule.controle_gastos.entities.Launch;
import com.chambule.controle_gastos.entities.enums.LaunchType;
import com.chambule.controle_gastos.entities.enums.PaymentMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class LaunchResponse {

    private Long id;
    private String description;
    private BigDecimal value;
    private LaunchType type;
    private LocalDate transactionDate;
    private PaymentMethod paymentMethod;
    private Long categoryId;
    private Long userId;

    public LaunchResponse(Launch launch) {
         this.id = launch.getId();
        this.description = launch.getDescription();
        this.value = launch.getValue();
        this.type = launch.getLaunchType();
        this.transactionDate = launch.getTransactionDate();
        this.paymentMethod = launch.getPaymentMethod();
        this.categoryId = launch.getCategory().getId();
        this.userId = launch.getUser().getId();

    }
}
