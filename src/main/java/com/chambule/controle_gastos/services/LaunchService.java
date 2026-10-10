package com.chambule.controle_gastos.services;
import com.chambule.controle_gastos.dto.launch.BalanceResponse;
import com.chambule.controle_gastos.dto.launch.LaunchRequest;
import com.chambule.controle_gastos.dto.launch.LaunchResponse;
import com.chambule.controle_gastos.entities.Category;
import com.chambule.controle_gastos.entities.Launch;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.entities.enums.LaunchType;
import com.chambule.controle_gastos.entities.enums.UserType;
import com.chambule.controle_gastos.exception.CategoryNotFound;
import com.chambule.controle_gastos.repository.CategoryRepository;
import com.chambule.controle_gastos.repository.LaunchRepository;
import com.chambule.controle_gastos.exception.ResourceNotFound;
import com.chambule.controle_gastos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaunchService {

    private final LaunchRepository launchRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public BalanceResponse findBalance(Long user_Id,Authentication authentication) {
        User user = userRepository.findById(user_Id).orElseThrow(() -> new ResourceNotFound(user_Id));

        validateUser(user, authentication);

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
        return new BalanceResponse(totalIncome, totalExpense, total);
    }

    public LaunchResponse createLaunch(LaunchRequest request) {
        Launch launch = new Launch();
        User user = currentUserService.getCurrentUser();

        Category category = findCategoryForUser(request.getCategoryId());

        validateCategoryType(category, request.getType());

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

    public List<LaunchResponse> findAll() {
        User user = currentUserService.getCurrentUser();
        List<Launch> launches = isAdmin(user)  ? launchRepository.findAll() : launchRepository.findByUser_Id(user.getId());
        return launches.stream().map(LaunchResponse::new).collect(Collectors.toList());
    }

    public LaunchResponse findById(Long id) {
        User user =  currentUserService.getCurrentUser();
        Launch launch = isAdmin(user) ? launchRepository.findById(id).orElseThrow(() -> new ResourceNotFound(id))
                : launchRepository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new ResourceNotFound(id));
        return new LaunchResponse(launch);
    }

    public List<LaunchResponse> findByCategoryId(Long categoryId) {
        User user = currentUserService.getCurrentUser();
        findAccessibleCategory(categoryId);

        List<Launch> launches = isAdmin(user)
                ? launchRepository.findByCategory_Id(categoryId)
                : launchRepository.findByUser_IdAndCategory_Id(user.getId(), categoryId);

        return launches.stream().map(LaunchResponse::new).collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        Launch launch = findLaunchForCurrentUser(id);
        currentUserService.assertOwnerOrAdmin(launch);
        launchRepository.delete(launch);
    }

    public LaunchResponse update(Long id, LaunchRequest request) {
        Launch launch = findLaunchForCurrentUser(id);

        Category category = findCategoryForUser(request.getCategoryId());

        validateCategoryType(category, request.getType());

        launch.setDescription(request.getDescription());
        launch.setValue(validateValue(request.getValue()));
        launch.setLaunchType(request.getType());
        launch.setPaymentMethod(request.getPaymentMethod());
        launch.setTransactionDate(request.getTransactionDate());
        launch.setCategory(category);

        return new LaunchResponse(launchRepository.save(launch));
    }

    private BigDecimal validateValue(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Value must be greater than zero");
        }
        return value;
    }

    private Launch findLaunchForCurrentUser(Long id) {
        User user = currentUserService.getCurrentUser();
        launchRepository.findById(id).orElseThrow(() -> new ResourceNotFound(id));
        return launchRepository.findByIdAndUser_Id(id, user.getId()).orElseThrow(() -> new ResourceNotFound(id));
    }

    private Category findCategoryForUser(Long categoryId) {
        User user =  currentUserService.getCurrentUser();
        return categoryRepository.findByIdAndUser_Id(categoryId, user.getId())
                .orElseThrow(() -> new ResourceNotFound(categoryId));
    }

    private Category findAccessibleCategory(Long categoryId) {
        User user =  currentUserService.getCurrentUser();
        if (isAdmin(user)) {
            return categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFound(categoryId));
        }

        return findCategoryForUser(categoryId);
    }

    private void validateCategoryType(Category category, LaunchType launchType) {
        if (!category.getType().name().equals(launchType.name())) {
            throw new CategoryNotFound("Category not found!");
        }

        throw new IllegalArgumentException("Category type is incompatible with launch type");
    }

    public  void   validateUser(User user, Authentication authentication){
        boolean users =  user.getLogin().equals(authentication.getName());
        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

         if(!users && !admin) throw new AccessDeniedException("Access Denied!");
    }

    public boolean isAdmin(User user){
        return  user.getRole() == UserType.ADMIN;
    }
}
