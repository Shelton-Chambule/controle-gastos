package com.chambule.controle_gastos.services;
import com.chambule.controle_gastos.dto.launch.LaunchRequestDTO;
import com.chambule.controle_gastos.dto.launch.LaunchResponseDTO;
import com.chambule.controle_gastos.entities.Category;
import com.chambule.controle_gastos.entities.Launch;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.entities.enums.LaunchType;
import com.chambule.controle_gastos.repository.CategoryRepository;
import com.chambule.controle_gastos.repository.LaunchRepository;
import com.chambule.controle_gastos.repository.UserRepository;
import com.chambule.controle_gastos.services.exception.DataBase;
import com.chambule.controle_gastos.services.exception.ResourceNotFound;
import com.chambule.controle_gastos.services.exception.ValueInvalid;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LaunchService {

    private final LaunchRepository launchRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public LaunchService(LaunchRepository launchRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.launchRepository = launchRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public BigDecimal validValue(BigDecimal value){
        if(value.compareTo(BigDecimal.ZERO)  <= 0){
            throw new ValueInvalid("Value invalid, the value must be greater than zero");
        }
        return value;
    }

    public LaunchResponseDTO saveIncome(LaunchRequestDTO launchRequestDTO) {
        Launch launch = new Launch();

        Category category = launchRepository.getReferenceById(launchRequestDTO.getCategoryId()).getCategory();
        User user = launchRepository.getReferenceById(launchRequestDTO.getUserId()).getUser();

        launch.setDescription(launchRequestDTO.getDescription());
        launch.setValue(validValue(launchRequestDTO.getValue()));
        launch.setLaunchType(launchRequestDTO.getType());   // receita
        launch.setTransactionDate(launchRequestDTO.getTransactionDate());
        launch.setCreationDate(LocalDate.now());
        launch.setPaymentMethod(launchRequestDTO.getPaymentMethod());
        launch.setCategory(category);
        launch.setUser(user);
        launchRepository.save(launch);
        return new LaunchResponseDTO(launch);
    }

    public List<LaunchResponseDTO> findAll() {
        List<Launch> launch = launchRepository.findAll();
        return launch.stream().map(LaunchResponseDTO::new).collect(Collectors.toList());
    }

    public LaunchResponseDTO findById(Long id) {
        Optional<Launch> launch = launchRepository.findById(id);
        return new LaunchResponseDTO(launch.orElseThrow(() -> new ResourceNotFound(id)));
    }

    // buscar lançamentos por categoria
    public List<LaunchResponseDTO> findByCategory(Long userId, Long categoryId) {
        if(!userRepository.existsById(userId)){
            throw new ResourceNotFound(userId);
        }

        if(!categoryRepository.existsById(categoryId)){
            throw new ResourceNotFound(categoryId);
        }

         List<Launch> launches =  launchRepository.findByCategory(userId, categoryId);
        return launches.stream().map(LaunchResponseDTO::new).collect(Collectors.toList());
    }

    public void  deleteById(Long id) {
        if (!launchRepository.existsById(id)) {
            throw new ResourceNotFound(id);
        }

       try {
           launchRepository.deleteById(id);
       }catch (InvalidDataAccessApiUsageException e) {
           throw new DataBase("This release is associated with a category");
       }
    }

    public LaunchResponseDTO update(Long id, LaunchRequestDTO launchRequestDTO) {
        try {
            Launch launch = launchRepository.getReferenceById(id);
            update(launch, launchRequestDTO);
            launchRepository.save(launch);
            return new LaunchResponseDTO(launch);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFound(id);
        }
    }

    private void update(Launch launch, LaunchRequestDTO launchRequestDTO) {
        launch.setDescription(launchRequestDTO.getDescription());
        launch.setValue(launchRequestDTO.getValue());
        launch.setLaunchType(LaunchType.EXPENSE);
        launch.setTransactionDate(launchRequestDTO.getTransactionDate());
    }
}
