package com.chambule.controle_gastos.services;
import com.chambule.controle_gastos.dto.category.CategoryRequest;
import com.chambule.controle_gastos.dto.category.CategoryResponse;
import com.chambule.controle_gastos.entities.Category;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.entities.enums.UserType;
import com.chambule.controle_gastos.repository.CategoryRepository;
import com.chambule.controle_gastos.exception.DataBase;
import com.chambule.controle_gastos.exception.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CurrentUserService currentUserService;

    public CategoryResponse createCategory(CategoryRequest categoryRequestDTO) {

        User user = currentUserService.getCurrentUser();

        Category category = new Category();
        category.setNameCategory(categoryRequestDTO.getNameCategory());
        category.setType(categoryRequestDTO.getType());
        category.setUser(user);

        categoryRepository.save(category);
        return new CategoryResponse(category);
    }

    public List<CategoryResponse> findAll() {
        User user = currentUserService.getCurrentUser();
        List<Category> categories = isAdmin(user) ? categoryRepository.findAll() : categoryRepository.findByUser_Id(user.getId());

        return categories.stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    public CategoryResponse findById(Long id,Authentication authentication) {
        Category category = findAccessibleCategory(id,authentication);
        return new CategoryResponse(category);
    }

    public CategoryResponse update(Long id, CategoryRequest categoryRequestDTO,Authentication authentication) {
        Category category = findAccessibleCategory(id,authentication);
        category.setNameCategory(categoryRequestDTO.getNameCategory());
        category.setType(categoryRequestDTO.getType());
        categoryRepository.save(category);
        return new CategoryResponse(category);
    }

    public void delete(Long id,Authentication authentication) {
        Category category = findAccessibleCategory(id,authentication);

        try {
            categoryRepository.delete(category);
        } catch (DataIntegrityViolationException exception) {
            throw new DataBase("The category is associated with a launch");
        }
    }

    private Category findAccessibleCategory(Long id, Authentication authentication) {
        User user = currentUserService.getCurrentUser();
        adminAndUser(user, authentication);

        categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFound(id));

        return categoryRepository.findByIdAndUser_Id(id, user.getId()).orElseThrow(() -> new ResourceNotFound(id));
    }

    public boolean isAdmin(User user){
        return  user.getRole() == UserType.ADMIN;
    }

    private void adminAndUser(User user, Authentication authentication) {
        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));
        boolean users = user.getLogin().equals(authentication.getName());

        if (!admin && !users) throw new AccessDeniedException("Access Denied!");
    }
}
