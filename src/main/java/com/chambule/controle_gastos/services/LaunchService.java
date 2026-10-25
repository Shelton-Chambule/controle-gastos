package com.chambule.controle_gastos.services;
import com.chambule.controle_gastos.dto.PageResponse;
import com.chambule.controle_gastos.dto.launch.BalanceResponse;
import com.chambule.controle_gastos.dto.launch.LaunchRequest;
import com.chambule.controle_gastos.dto.launch.LaunchResponse;
import com.chambule.controle_gastos.entities.Category;
import com.chambule.controle_gastos.entities.Launch;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.entities.enums.CategoryType;
import com.chambule.controle_gastos.entities.enums.LaunchType;
import com.chambule.controle_gastos.entities.enums.UserType;
import com.chambule.controle_gastos.mapper.LaunchMapper;
import com.chambule.controle_gastos.repository.CategoryRepository;
import com.chambule.controle_gastos.repository.LaunchRepository;
import com.chambule.controle_gastos.exception.CategoryAlreadyExistsException;
import com.chambule.controle_gastos.exception.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaunchService {

    private final LaunchRepository launchRepository;
    private final CategoryRepository categoryRepository;
    private final CurrentUserService currentUserService;

    @Transactional
    public LaunchResponse createLaunch(LaunchRequest request, User user) {

        Category category = createCategory(request, user);

        Launch launch = new Launch();
        launch.setDescription(request.getDescription());
        launch.setValue(validateValue(request.getValue()));
        launch.setLaunchType(request.getType());
        launch.setTransactionDate(request.getTransactionDate());
        launch.setPaymentMethod(request.getPaymentMethod());
        launch.setCategory(category);
        launch.setUser(user);

        Launch savedLaunch = launchRepository.save(launch);
        return new LaunchResponse(savedLaunch);
    }

    public BalanceResponse findBalance(Pageable pageable) {
        User user = currentUserService.getCurrentUser();

        Page<Launch> launches = isAdmin(user)
                ? launchRepository.findAll(pageable)
                : launchRepository.findByUser_Id(user.getId(), pageable);

        BigDecimal totalIncome = sumByType(launches, LaunchType.INCOME);
        BigDecimal totalExpense = sumByType(launches, LaunchType.EXPENSE);

        return new BalanceResponse(totalIncome, totalExpense, totalIncome.subtract(totalExpense));
    }

    public PageResponse<LaunchResponse> findAll(Pageable pageable) {
        User user = currentUserService.getCurrentUser();

        Page<Launch> launches = isAdmin(user)
                ? launchRepository.findAll(pageable)
                : launchRepository.findByUser_Id(user.getId(), pageable);

        Page<LaunchResponse> launchResponses = launches.map(LaunchMapper::toLaunch);
        return PageResponse.from(launchResponses);
    }

    public LaunchResponse findById(Long id) {
        Launch launch = findLaunchForCurrentUser(id);
        return new LaunchResponse(launch);
    }

    public List<LaunchResponse> findByCategoryId(Long categoryId) {
        User user = currentUserService.getCurrentUser();
        Category category = findAccessibleCategory(categoryId, user);

        List<Launch> launches = isAdmin(user)
                ? launchRepository.findByCategory_Id(category.getId())
                : launchRepository.findByUser_IdAndCategory_Id(user.getId(), category.getId());

        return launches.stream().map(LaunchResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(Long id) {
        Launch launch = findLaunchForCurrentUser(id);
        launchRepository.delete(launch);
    }

    @Transactional
    public LaunchResponse update(Long id, LaunchRequest request) {
        User user = currentUserService.getCurrentUser();
        Launch launch = findLaunchForCurrentUser(id, user);

        Category category = createCategory(request, user);

        launch.setDescription(request.getDescription());
        launch.setValue(validateValue(request.getValue()));
        launch.setLaunchType(request.getType());
        launch.setPaymentMethod(request.getPaymentMethod());
        launch.setTransactionDate(request.getTransactionDate());
        launch.setCategory(category);

        return new LaunchResponse(launchRepository.save(launch));
    }

    private Category createCategory(LaunchRequest request, User user) {
        categoryRepository.findByNameCategoryAndUser(request.getNameCategory(), user)
                .ifPresent(existing -> {
                    throw new CategoryAlreadyExistsException(request.getNameCategory());
                });

        Category newCategory = new Category();
        newCategory.setNameCategory(request.getNameCategory());
        newCategory.setType(CategoryType.valueOf(request.getType().name()));
        newCategory.setUser(user);
        return categoryRepository.save(newCategory);
    }

    private BigDecimal sumByType(Page<Launch> launches, LaunchType type) {
        return launches.stream()
                .filter(launch -> launch.getLaunchType() == type)
                .map(Launch::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal validateValue(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Value must be greater than zero");
        }
        return value;
    }

    private Launch findLaunchForCurrentUser(Long id) {
        return findLaunchForCurrentUser(id, currentUserService.getCurrentUser());
    }

    private Launch findLaunchForCurrentUser(Long id, User user) {
        return isAdmin(user)
                ? launchRepository.findById(id).orElseThrow(() -> new ResourceNotFound(id))
                : launchRepository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new ResourceNotFound(id));
    }

    private Category findAccessibleCategory(Long categoryId, User user) {
        return isAdmin(user)
                ? categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFound(categoryId))
                : categoryRepository.findByIdAndUser_Id(categoryId, user.getId())
                .orElseThrow(() -> new ResourceNotFound(categoryId));
    }

    private void validateCategoryType(Category category, LaunchType launchType) {
        if (!category.getType().name().equals(launchType.name())) {
            throw new IllegalArgumentException("Category type is incompatible with launch type");
        }
    }

    public boolean isAdmin(User user) {
        return user.getRole() == UserType.ADMIN;
    }
}
