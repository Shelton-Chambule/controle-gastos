package com.chambule.controle_gastos.dto.launch;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class BalanceResponse {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal total;

    public BalanceResponse(BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal total) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.total = total;
    }
}
