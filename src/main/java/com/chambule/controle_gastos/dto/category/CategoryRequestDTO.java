package com.chambule.controle_gastos.dto.category;

import com.chambule.controle_gastos.entities.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoryRequestDTO {

    @NotBlank(message = "Required field")
    private String nameCategory;

    @NotNull(message = "Required field")
    private CategoryType type;

    public CategoryRequestDTO() {
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
}
