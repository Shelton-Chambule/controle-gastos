package com.chambule.controle_gastos.entities;
import com.chambule.controle_gastos.entities.enums.LaunchType;
import com.chambule.controle_gastos.entities.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;


@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_launch")
public class Launch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "launch_Id")
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    private LaunchType launchType;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Launch(Long id, String description, BigDecimal value, LaunchType launchType, LocalDate transactionDate, PaymentMethod paymentMethod, LocalDate creationDate) {
        this.id = id;
        this.description = description;
        this.value = value;
        this.launchType = launchType;
        this.transactionDate = transactionDate;
        this.paymentMethod = paymentMethod;
        this.creationDate = creationDate;
    }

    @PrePersist
    public void creationDate(){
        this.creationDate = LocalDate.now();
    }
}
