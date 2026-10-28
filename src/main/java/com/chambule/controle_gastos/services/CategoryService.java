package com.chambule.controle_gastos.services;
import com.chambule.controle_gastos.dto.PageResponse;
import com.chambule.controle_gastos.dto.category.CategoryRequest;
import com.chambule.controle_gastos.dto.category.CategoryResponse;
import com.chambule.controle_gastos.entities.Category;
import com.chambule.controle_gastos.mapper.CategoryMapper;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.entities.enums.UserType;
import com.chambule.controle_gastos.repository.CategoryRepository;
import com.chambule.controle_gastos.exception.DataBase;
import com.chambule.controle_gastos.exception.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CurrentUserService currentUserService;

    public PageResponse<CategoryResponse> findAll(Pageable pageable) {
        log.info("Listing categories, page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        User user = currentUserService.getCurrentUser();
        log.debug("Authenticated user id={}, admin={}", user.getId(), isAdmin(user));

        Page<Category> categoryPage = isAdmin(user)
                ? categoryRepository.findAll(pageable)
                : categoryRepository.findByUser_Id(user.getId(), pageable);

        Page<CategoryResponse> responsePage = categoryPage.map(CategoryMapper::toResponse);
        log.info("Returning {} categorie(s) out of {} total", responsePage.getNumberOfElements(), responsePage.getTotalElements());
        return PageResponse.from(responsePage);
    }

    public CategoryResponse findById(Long id, Authentication authentication) {
        log.info("Fetching category id={}", id);
        Category category = findAccessibleCategory(id, authentication);
        log.debug("Category id={} found: name='{}'", id, category.getNameCategory());
        return new CategoryResponse(category);
    }

    public CategoryResponse update(Long id, CategoryRequest categoryRequestDTO, Authentication authentication) {
        log.info("Updating category id={}", id);
        Category category = findAccessibleCategory(id, authentication);

        log.debug("Category id={} before update: name='{}', type={}", id, category.getNameCategory(), category.getType());
        category.setNameCategory(categoryRequestDTO.getNameCategory());
        category.setType(categoryRequestDTO.getType());
        categoryRepository.save(category);
        log.info("Category id={} updated: name='{}', type={}", id, category.getNameCategory(), category.getType());

        return new CategoryResponse(category);
    }

    public void delete(Long id, Authentication authentication) {
        log.info("Deleting category id={}", id);
        Category category = findAccessibleCategory(id, authentication);

        try {
            categoryRepository.delete(category);
            log.info("Category id={} deleted successfully", id);
        } catch (DataIntegrityViolationException exception) {
            log.warn("Cannot delete category id={}: associated with a launch", id);
            throw new DataBase("The category is associated with a launch");
        }
    }

    private Category findAccessibleCategory(Long id, Authentication authentication) {
        User user = currentUserService.getCurrentUser();
        adminAndUser(user, authentication);

        categoryRepository.findById(id).orElseThrow(() -> {
            log.warn("Category id={} not found", id);
            return new ResourceNotFound(id);
        });

        return categoryRepository.findByIdAndUser_Id(id, user.getId()).orElseThrow(() -> {
            log.warn("Category id={} not accessible for user id={}", id, user.getId());
            return new ResourceNotFound(id);
        });
    }

    public boolean isAdmin(User user) {
        return user.getRole() == UserType.ADMIN;
    }

    private void adminAndUser(User user, Authentication authentication) {
        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));
        boolean users = user.getLogin().equals(authentication.getName());

        if (!admin && !users) {
            log.warn("Access denied for login='{}' on category operation", authentication.getName());
            throw new AccessDeniedException("Access Denied!");
        }
    }
}