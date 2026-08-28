package com.chambule.controle_gastos.entities;
import com.chambule.controle_gastos.entities.enums.LaunchType;
import com.chambule.controle_gastos.entities.enums.PaymentMethod;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_launch")
public class Launch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    private LaunchType launchType;
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private LocalDate creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Launch(){}

    public Launch(Long id, String description, BigDecimal value, LaunchType launchType, LocalDate transactionDate, PaymentMethod paymentMethod, LocalDate creationDate) {
        this.id = id;
        this.description = description;
        this.value = value;
        this.launchType = launchType;
        this.transactionDate = transactionDate;
        this.paymentMethod = paymentMethod;
        this.creationDate = creationDate;
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

    public LaunchType getLaunchType() {
        return launchType;
    }

    public void setLaunchType(LaunchType launchType) {
        this.launchType = launchType;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @PrePersist
    public void creationDate(){
        this.creationDate = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Launch launch = (Launch) o;
        return Objects.equals(id, launch.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
