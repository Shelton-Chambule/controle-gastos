package com.chambule.controle_gastos.dto.category;
import com.chambule.controle_gastos.entities.Category;
import com.chambule.controle_gastos.entities.enums.CategoryType;
public class CategoryResponseDTO {

    private Long categoryId;
    private String nameCategory;
    private Long userId;
    private CategoryType type;

    public CategoryResponseDTO(){}

    public CategoryResponseDTO(Category category) {
        this.categoryId = category.getIdCategory();
        this.nameCategory = category.getNameCategory() ;
        this.type = category.getType();
        this.userId = category.getUser().getId();
    }

    public Long getIdCategory() {
        return categoryId;
    }

    public void setIdCategory(Long idCategory) {
        this.categoryId = idCategory;
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

    public void setIdUser(Long idUser) {
        this.userId = idUser;
    }
}
