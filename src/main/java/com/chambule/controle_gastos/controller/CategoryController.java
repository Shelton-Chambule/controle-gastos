package com.chambule.controle_gastos.controller;
import com.chambule.controle_gastos.dto.PageResponse;
import com.chambule.controle_gastos.dto.category.CategoryRequest;
import com.chambule.controle_gastos.dto.category.CategoryResponse;
import com.chambule.controle_gastos.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/getAll")
    public ResponseEntity<PageResponse<CategoryResponse>> getAllCategories(Pageable pageable){
       log.info("GET /categories");
        return ResponseEntity.ok().body(categoryService.findAll(pageable));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getOneCategory(@PathVariable Long categoryId, Authentication authentication){
        log.info("GET  /categories/{}", categoryId);
        return ResponseEntity.ok().body(categoryService.findById(categoryId,authentication));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> deleteOneCategories(@PathVariable Long categoryId, Authentication authentication){
        log.info("DELETE /categories/{}", categoryId);
        categoryService.delete(categoryId,authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategories(@PathVariable Long categoryId, @Valid @RequestBody CategoryRequest categoryRequestDTO, Authentication authentication){
        log.info("PUT /categories/{}", categoryId);
        return ResponseEntity.ok().body(categoryService.update(categoryId,categoryRequestDTO,authentication));
    }

}
