package com.chambule.controle_gastos.controller;
import com.chambule.controle_gastos.dto.PageResponse;
import com.chambule.controle_gastos.dto.category.CategoryRequest;
import com.chambule.controle_gastos.dto.category.CategoryResponse;
import com.chambule.controle_gastos.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/getAll")
    public ResponseEntity<PageResponse<CategoryResponse>> getAllCategories(Pageable pageable){
        return ResponseEntity.ok().body(categoryService.findAll(pageable));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getOneCategory(@PathVariable Long categoryId, Authentication authentication){
        return ResponseEntity.ok().body(categoryService.findById(categoryId,authentication));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> deleteOneCategories(@PathVariable Long categoryId, Authentication authentication){
        categoryService.delete(categoryId,authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategories(@PathVariable Long categoryId, @Valid @RequestBody CategoryRequest categoryRequestDTO, Authentication authentication){
        return ResponseEntity.ok().body(categoryService.update(categoryId,categoryRequestDTO,authentication));
    }

}
