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
    private Long categoryId;
    private String nameCategory;
    @Enumerated(EnumType.STRING)
    private CategoryType type;   // despesa ou receita;// id do propretario que criou a categoria
    private LocalDate creationDate;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @OneToMany(mappedBy = "category")
    private Set<Launch> launches = new HashSet<>();

    public Category(){}

    public Category(Long categoryId, String nameCategory, CategoryType type, LocalDate creationDate) {
        this.categoryId = categoryId;
        this.nameCategory = nameCategory;
        this.type = type;
        this.creationDate = creationDate;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(categoryId, category.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(categoryId);
    }
}
