package com.chambule.controle_gastos.entities;
import com.chambule.controle_gastos.entities.enums.CategoryType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String nameCategory;

    @Enumerated(EnumType.STRING)
    private CategoryType type;   // despesa ou receita  // id do propretario que criou a categoria

    private LocalDate creationDate;
    private LocalDate updateDate;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @OneToMany(mappedBy = "category")
    private Set<Launch> launches = new HashSet<>();

    public Category(){}

    public Category(Long id, String nameCategory, CategoryType type, LocalDate creationDate, LocalDate updateDate) {
        Id = id;
        this.nameCategory = nameCategory;
        this.type = type;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
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

    public Set<Launch> getLaunches() {
        return launches;
    }

    @PrePersist
    public void creationDate(){
        this.creationDate = LocalDate.now();
    }

    @PreUpdate
    public  void updateDate(){
        this.updateDate = LocalDate.now();
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(Id, category.Id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(Id);
    }
}
