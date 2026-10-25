package com.chambule.controle_gastos.mapper;
import com.chambule.controle_gastos.dto.category.CategoryResponse;
import com.chambule.controle_gastos.entities.Category;

public class CategoryMapper {

    public  static CategoryResponse toResponse(Category category){
        CategoryResponse response = new CategoryResponse();
        response.setCategoryId(category.getId());
        response.setNameCategory(category.getNameCategory());

        return  response;
    }
}
