package com.chambule.controle_gastos.dto.category;
import com.chambule.controle_gastos.entities.Category;
import com.chambule.controle_gastos.entities.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoryRequestDTO {

    @NotBlank(message = "Campo obrigatório")
    private String nameCategory;

    @NotNull(message = "Campo obrigatório")
    private Integer type;  // despesa ou receita;

    @NotNull(message = "Campo obrigatório")
    private Long userId;

    public CategoryRequestDTO(){}

    public CategoryRequestDTO(Category category) {
        this.nameCategory = category.getNameCategory();
        setType(category.getType());
        this.userId = category.getUser().getId();
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public CategoryType getType() {
        return CategoryType.category(type);
    }

    public void setType(CategoryType type) {
        this.type = type.getCode();
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
