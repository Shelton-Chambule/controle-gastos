package com.chambule.controle_gastos.controller;
import com.chambule.controle_gastos.dto.category.CategoryRequestDTO;
import com.chambule.controle_gastos.dto.category.CategoryResponseDTO;
import com.chambule.controle_gastos.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/createCategory")
    public ResponseEntity<CategoryResponseDTO> createCategory( @Valid  @RequestBody CategoryRequestDTO categoryRequestDTO){
        CategoryResponseDTO category = categoryService.createCategory(categoryRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll(){
        List<CategoryResponseDTO> category = categoryService.findAll();
        return ResponseEntity.ok().body(category);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable Long id){
        CategoryResponseDTO category = categoryService.findById(id);
        return ResponseEntity.ok().body(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> delete(@PathVariable Long id){
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CategoryRequestDTO categoryRequestDTO){
        CategoryResponseDTO category = categoryService.update(id,categoryRequestDTO);
        return ResponseEntity.ok().body(category);
    }

}
