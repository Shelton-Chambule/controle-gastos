package com.chambule.controle_gastos.dto.launch;
import java.math.BigDecimal;
public class BalanceResponseDTO {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal total;

    public BalanceResponseDTO(){}

    public BalanceResponseDTO(BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal total) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.total = total;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
