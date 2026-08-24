package com.chambule.controle_gastos.dto.category;
import com.chambule.controle_gastos.entities.Category;
import com.chambule.controle_gastos.entities.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoryRequestDTO {

    @NotBlank(message = "Campo obrigatório")
    private String nameCategory;

    @NotNull(message = "Campo obrigatório")
    private CategoryType type;// despesa ou receita;

    public CategoryRequestDTO(){}

    public CategoryRequestDTO(Category category) {
        this.nameCategory = category.getNameCategory();
        this.type = category.getType();
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
