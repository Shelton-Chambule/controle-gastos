package com.chambule.controle_gastos.services;

import com.chambule.controle_gastos.dto.launch.BalanceResponseDTO;
import com.chambule.controle_gastos.dto.launch.LaunchRequestDTO;
import com.chambule.controle_gastos.dto.launch.LaunchResponseDTO;
import com.chambule.controle_gastos.entities.Category;
import com.chambule.controle_gastos.entities.Launch;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.entities.enums.LaunchType;
import com.chambule.controle_gastos.repository.CategoryRepository;
import com.chambule.controle_gastos.repository.LaunchRepository;
import com.chambule.controle_gastos.services.exception.ResourceNotFound;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LaunchService {

    private final LaunchRepository launchRepository;
    private final CategoryRepository categoryRepository;

    public LaunchService(
            LaunchRepository launchRepository,
            CategoryRepository categoryRepository,
    ) {
        this.launchRepository = launchRepository;
        this.categoryRepository = categoryRepository;
    }

    public BalanceResponseDTO findBalance() {
        User user = currentUserService.get();
        List<Launch> launches = launchRepository.findByUser_Id(user.getId());

        BigDecimal totalIncome = launches.stream()
                .filter(launch -> launch.getLaunchType() == LaunchType.INCOME)
                .map(Launch::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = launches.stream()
                .filter(launch -> launch.getLaunchType() == LaunchType.EXPENSE)
                .map(Launch::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal total = totalIncome.subtract(totalExpense);
        return new BalanceResponseDTO(totalIncome, totalExpense, total);
    }

    public LaunchResponseDTO createLaunch(LaunchRequestDTO request) {
        User user = currentUserService.get();
        Category category = findCategoryForUser(request.getCategoryId(), user);

        validateCategoryType(category, request.getType());

        Launch launch = new Launch();
        launch.setDescription(request.getDescription());
        launch.setValue(validateValue(request.getValue()));
        launch.setLaunchType(request.getType());
        launch.setTransactionDate(request.getTransactionDate());
        launch.setPaymentMethod(request.getPaymentMethod());
        launch.setCategory(category);
        launch.setUser(user);

        Launch savedLaunch = launchRepository.save(launch);
        return new LaunchResponseDTO(savedLaunch);
    }

    public List<LaunchResponseDTO> findAll() {
        User user = currentUserService.get();
        List<Launch> launches = isAdmin(user)
                ? launchRepository.findAll()
                : launchRepository.findByUser_Id(user.getId());

        return launches.stream()
                .map(LaunchResponseDTO::new)
                .collect(Collectors.toList());
    }

    public LaunchResponseDTO findById(Long id) {
        User user = currentUserService.get();
        Launch launch = isAdmin(user)
                ? launchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(id))
                : launchRepository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new ResourceNotFound(id));

        return new LaunchResponseDTO(launch);
    }

    public List<LaunchResponseDTO> findByCategoryId(Long categoryId) {
        User user = currentUserService.get();
        findAccessibleCategory(categoryId, user);

        List<Launch> launches = isAdmin(user)
                ? launchRepository.findByCategory_Id(categoryId)
                : launchRepository.findByUser_IdAndCategory_Id(user.getId(), categoryId);

        return launches.stream()
                .map(LaunchResponseDTO::new)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        Launch launch = findLaunchForCurrentUser(id);
        launchRepository.delete(launch);
    }

    public LaunchResponseDTO update(Long id, LaunchRequestDTO request) {
        User user = currentUserService.get();
        Launch launch = findLaunchForCurrentUser(id);
        Category category = findCategoryForUser(request.getCategoryId(), user);

        validateCategoryType(category, request.getType());

        launch.setDescription(request.getDescription());
        launch.setValue(validateValue(request.getValue()));
        launch.setLaunchType(request.getType());
        launch.setPaymentMethod(request.getPaymentMethod());
        launch.setTransactionDate(request.getTransactionDate());
        launch.setCategory(category);

        return new LaunchResponseDTO(launchRepository.save(launch));
    }

    private BigDecimal validateValue(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Value must be greater than zero");
        }
        return value;
    }

    private Launch findLaunchForCurrentUser(Long id) {
        User user = currentUserService.get();

        if (isAdmin(user)) {
            return launchRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFound(id));
        }

        return launchRepository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new ResourceNotFound(id));
    }

    private Category findCategoryForUser(Long categoryId, User user) {
        return categoryRepository.findByIdAndUser_Id(categoryId, user.getId())
                .orElseThrow(() -> new ResourceNotFound(categoryId));
    }

    private Category findAccessibleCategory(Long categoryId, User user) {
        if (isAdmin(user)) {
            return categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFound(categoryId));
        }

        return findCategoryForUser(categoryId, user);
    }

    private void validateCategoryType(Category category, LaunchType launchType) {
        if (category.getType().name().equals(launchType.name())) {
            return;
        }

        throw new IllegalArgumentException("Category type is incompatible with launch type");
    }

    private boolean isAdmin(User user) {
        return user.getRole().name().equals("ADMIN");
    }
}
