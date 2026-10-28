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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaunchService {

    private final LaunchRepository launchRepository;
    private final CategoryRepository categoryRepository;
    private final CurrentUserService currentUserService;

    @Transactional
    public LaunchResponse createLaunch(LaunchRequest request, User user) {
        log.info("Creating launch for user id={}, description='{}', value={}",
                user.getId(), request.getDescription(), request.getValue());

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
        log.info("Launch id={} created successfully for user id={}", savedLaunch.getId(), user.getId());

        return new LaunchResponse(savedLaunch);
    }

    public BalanceResponse findBalance(Pageable pageable) {
        User user = currentUserService.getCurrentUser();
        log.info("Calculating balance for user id={}", user.getId());

        Page<Launch> launches = isAdmin(user)
                ? launchRepository.findAll(pageable)
                : launchRepository.findByUser_Id(user.getId(), pageable);

        BigDecimal totalIncome = sumByType(launches, LaunchType.INCOME);
        BigDecimal totalExpense = sumByType(launches, LaunchType.EXPENSE);

        log.debug("Balance for user id={}: income={}, expense={}", user.getId(), totalIncome, totalExpense);
        return new BalanceResponse(totalIncome, totalExpense, totalIncome.subtract(totalExpense));
    }

    public PageResponse<LaunchResponse> findAll(Pageable pageable) {
        User user = currentUserService.getCurrentUser();
        log.info("Listing launches for user id={}, page={}, size={}",
                user.getId(), pageable.getPageNumber(), pageable.getPageSize());

        Page<Launch> launches = isAdmin(user)
                ? launchRepository.findAll(pageable)
                : launchRepository.findByUser_Id(user.getId(), pageable);

        Page<LaunchResponse> launchResponses = launches.map(LaunchMapper::toLaunch);
        log.info("Returning {} launch(es) out of {} total", launchResponses.getNumberOfElements(), launchResponses.getTotalElements());
        return PageResponse.from(launchResponses);
    }

    public LaunchResponse findById(Long id) {
        log.info("Fetching launch id={}", id);
        Launch launch = findLaunchForCurrentUser(id);
        return new LaunchResponse(launch);
    }

    public List<LaunchResponse> findByCategoryId(Long categoryId) {
        User user = currentUserService.getCurrentUser();
        log.info("Listing launches for user id={} by category id={}", user.getId(), categoryId);
        Category category = findAccessibleCategory(categoryId, user);

        List<Launch> launches = isAdmin(user)
                ? launchRepository.findByCategory_Id(category.getId())
                : launchRepository.findByUser_IdAndCategory_Id(user.getId(), category.getId());

        log.debug("Found {} launch(es) for category id={}", launches.size(), categoryId);
        return launches.stream().map(LaunchResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(Long id) {
        log.info("Deleting launch id={}", id);
        Launch launch = findLaunchForCurrentUser(id);
        launchRepository.delete(launch);
        log.info("Launch id={} deleted successfully", id);
    }

    @Transactional
    public LaunchResponse update(Long id, LaunchRequest request) {
        log.info("Updating launch id={}", id);
        User user = currentUserService.getCurrentUser();
        Launch launch = findLaunchForCurrentUser(id, user);

        Category category = createCategory(request, user);

        launch.setDescription(request.getDescription());
        launch.setValue(validateValue(request.getValue()));
        launch.setLaunchType(request.getType());
        launch.setPaymentMethod(request.getPaymentMethod());
        launch.setTransactionDate(request.getTransactionDate());
        launch.setCategory(category);

        LaunchResponse response = new LaunchResponse(launchRepository.save(launch));
        log.info("Launch id={} updated successfully", id);
        return response;
    }

    private Category createCategory(LaunchRequest request, User user) {
        categoryRepository.findByNameCategoryAndUser(request.getNameCategory(), user)
                .ifPresent(existing -> {
                    log.warn("Category creation rejected: '{}' already exists for user id={}",
                            request.getNameCategory(), user.getId());
                    throw new CategoryAlreadyExistsException(request.getNameCategory());
                });

        Category newCategory = new Category();
        newCategory.setNameCategory(request.getNameCategory());
        newCategory.setType(CategoryType.valueOf(request.getType().name()));
        newCategory.setUser(user);
        Category saved = categoryRepository.save(newCategory);
        log.debug("Category id={} created implicitly from launch, name='{}'", saved.getId(), saved.getNameCategory());
        return saved;
    }

    private BigDecimal sumByType(Page<Launch> launches, LaunchType type) {
        return launches.stream()
                .filter(launch -> launch.getLaunchType() == type)
                .map(Launch::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal validateValue(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Launch rejected: invalid value='{}'", value);
            throw new IllegalArgumentException("Value must be greater than zero");
        }
        return value;
    }

    private Launch findLaunchForCurrentUser(Long id) {
        return findLaunchForCurrentUser(id, currentUserService.getCurrentUser());
    }

    private Launch findLaunchForCurrentUser(Long id, User user) {
        return isAdmin(user)
                ? launchRepository.findById(id).orElseThrow(() -> {
            log.warn("Launch id={} not found", id);
            return new ResourceNotFound(id);
        })
                : launchRepository.findByIdAndUser_Id(id, user.getId()).orElseThrow(() -> {
            log.warn("Launch id={} not found or not accessible for user id={}", id, user.getId());
            return new ResourceNotFound(id);
        });
    }

    private Category findAccessibleCategory(Long categoryId, User user) {
        return isAdmin(user)
                ? categoryRepository.findById(categoryId).orElseThrow(() -> {
            log.warn("Category id={} not found", categoryId);
            return new ResourceNotFound(categoryId);
        })
                : categoryRepository.findByIdAndUser_Id(categoryId, user.getId()).orElseThrow(() -> {
            log.warn("Category id={} not found or not accessible for user id={}", categoryId, user.getId());
            return new ResourceNotFound(categoryId);
        });
    }

    private void validateCategoryType(Category category, LaunchType launchType) {
        if (!category.getType().name().equals(launchType.name())) {
            log.warn("Category type mismatch: category type={}, launch type={}", category.getType(), launchType);
            throw new IllegalArgumentException("Category type is incompatible with launch type");
        }
    }

    public boolean isAdmin(User user) {
        return user.getRole() == UserType.ADMIN;
    }
}