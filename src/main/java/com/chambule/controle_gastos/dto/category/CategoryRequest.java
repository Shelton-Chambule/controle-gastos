package com.chambule.controle_gastos.dto.category;
import com.chambule.controle_gastos.entities.Category;
import com.chambule.controle_gastos.entities.enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CategoryRequest {

    @JsonProperty(required = true)
    @NotBlank(message = "Required field")
    private String nameCategory;

    @JsonProperty(required = true)
    @NotNull(message = "Required field")
    private CategoryType type;

    @JsonProperty(required = true)
    @NotNull(message = "Required field")
    private Long userId;

    public CategoryRequest(Category category) {
        this.nameCategory = category.getNameCategory();
        this.type = category.getType();
        this.userId = category.getUser().getId();
    }
}
