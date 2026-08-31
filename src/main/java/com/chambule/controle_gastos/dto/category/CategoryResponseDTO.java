package com.chambule.controle_gastos.dto.category;
import com.chambule.controle_gastos.entities.Category;
import com.chambule.controle_gastos.entities.enums.CategoryType;

import java.time.LocalDate;

public class CategoryResponseDTO {

    private Long categoryId;
    private String nameCategory;
    private Long userId;
    private CategoryType type;
    private LocalDate creationDate;

    public CategoryResponseDTO(){}

    public CategoryResponseDTO(Category category) {
        this.categoryId = category.getId();
        this.nameCategory = category.getNameCategory() ;
        this.type = category.getType();
        this.userId = category.getUser().getId();
        this.creationDate = category.getCreationDate();
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

    public Long getIdUser() {
        return userId;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public void setIdUser(Long idUser) {
        this.userId = idUser;
    }
}
