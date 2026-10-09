package com.chambule.controle_gastos.controller;
import com.chambule.controle_gastos.dto.category.CategoryRequest;
import com.chambule.controle_gastos.dto.category.CategoryResponse;
import com.chambule.controle_gastos.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/save")
    public ResponseEntity<CategoryResponse> createCategory(@Valid  @RequestBody CategoryRequest categoryRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryRequestDTO));
    }

    @GetMapping("getAll")
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){
        return ResponseEntity.ok().body(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getOneCategory(@PathVariable Long id, Authentication authentication){
        return ResponseEntity.ok().body(categoryService.findById(id,authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponse> deleteOneCategories(@PathVariable Long id, Authentication authentication){
        categoryService.delete(id,authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategories(@PathVariable Long id, @Valid @RequestBody CategoryRequest categoryRequestDTO, Authentication authentication){
        return ResponseEntity.ok().body(categoryService.update(id,categoryRequestDTO,authentication));
    }

}
