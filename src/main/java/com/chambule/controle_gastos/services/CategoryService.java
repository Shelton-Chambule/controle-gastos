package com.chambule.controle_gastos.services;

import com.chambule.controle_gastos.dto.category.CategoryRequestDTO;
import com.chambule.controle_gastos.dto.category.CategoryResponseDTO;
import com.chambule.controle_gastos.entities.Category;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.repository.CategoryRepository;
import com.chambule.controle_gastos.services.exception.DataBase;
import com.chambule.controle_gastos.services.exception.ResourceNotFound;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CurrentUserService currentUserService;

    public CategoryService(
            CategoryRepository categoryRepository,
            CurrentUserService currentUserService
    ) {
        this.categoryRepository = categoryRepository;
        this.currentUserService = currentUserService;
    }

    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        User user = currentUserService.get();

        Category category = new Category();
        category.setNameCategory(categoryRequestDTO.getNameCategory());
        category.setType(categoryRequestDTO.getType());
        category.setUser(user);

        categoryRepository.save(category);
        return new CategoryResponseDTO(category);
    }

    public List<CategoryResponseDTO> findAll() {
        User user = currentUserService.get();
        List<Category> categories = isAdmin(user)
                ? categoryRepository.findAll()
                : categoryRepository.findByUser_Id(user.getId());

        return categories.stream()
                .map(CategoryResponseDTO::new)
                .collect(Collectors.toList());
    }

    public CategoryResponseDTO findById(Long id) {
        Category category = findAccessibleCategory(id);
        return new CategoryResponseDTO(category);
    }

    public CategoryResponseDTO update(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category category = findAccessibleCategory(id);
        category.setNameCategory(categoryRequestDTO.getNameCategory());
        category.setType(categoryRequestDTO.getType());
        categoryRepository.save(category);
        return new CategoryResponseDTO(category);
    }

    public void delete(Long id) {
        Category category = findAccessibleCategory(id);

        try {
            categoryRepository.delete(category);
        } catch (DataIntegrityViolationException exception) {
            throw new DataBase("The category is associated with a launch");
        }
    }

    private Category findAccessibleCategory(Long id) {
        User user = currentUserService.get();

        if (isAdmin(user)) {
            return categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFound(id));
        }

        return categoryRepository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new ResourceNotFound(id));
    }

    private boolean isAdmin(User user) {
        return user.getRole().name().equals("ADMIN");
    }
}
