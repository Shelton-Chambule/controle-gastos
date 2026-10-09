package com.chambule.controle_gastos.entities;
import com.chambule.controle_gastos.entities.enums.CategoryType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "tb_category")
@EqualsAndHashCode(of = "id")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_Id")
    private Long id;

    @Column(name = "name_category",nullable = false)
    private String nameCategory;

    @Enumerated(EnumType.STRING)
    private CategoryType type;   // despesa ou receita  // id do propretario que criou a categoria

    @Column(name ="creation_date",nullable = false)
    private LocalDate creationDate;

    @Column(name = "update_date",nullable = false)
    private LocalDate updateDate;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @OneToMany(mappedBy = "category")
    private Set<Launch> launches = new HashSet<>();

    public Category(Long id, String nameCategory, CategoryType type, LocalDate creationDate, LocalDate updateDate) {
        this.id = id;
        this.nameCategory = nameCategory;
        this.type = type;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    @PrePersist
    public void creationDate(){
        this.creationDate = LocalDate.now();
    }

    @PreUpdate
    public  void updateDate(){
        this.updateDate = LocalDate.now();
    }
}
