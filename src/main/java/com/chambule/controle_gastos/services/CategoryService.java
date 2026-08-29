package com.chambule.controle_gastos.services;
import com.chambule.controle_gastos.dto.category.CategoryRequestDTO;
import com.chambule.controle_gastos.dto.category.CategoryResponseDTO;
import com.chambule.controle_gastos.entities.Category;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.repository.CategoryRepository;
import com.chambule.controle_gastos.repository.UserRepository;
import com.chambule.controle_gastos.services.exception.DataBase;
import com.chambule.controle_gastos.services.exception.ResourceNotFound;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private  final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

        public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO){
            User userId = userRepository.getReferenceById(categoryRequestDTO.getUserId());

            Category category = new Category();
            category.setNameCategory(categoryRequestDTO.getNameCategory());
            category.setType(categoryRequestDTO.getType());
            category.setCreationDate(LocalDate.now());
            category.setUser(categoryRequestDTO);
            categoryRepository.save(category);
            return new CategoryResponseDTO(category);
        }

        public List<CategoryResponseDTO> findAll(){
            List<Category> categories = categoryRepository.findAll();
            return categories.stream().map(CategoryResponseDTO::new).collect(Collectors.toList());
        }

        public CategoryResponseDTO findById(Long id){
            Optional<Category> category = categoryRepository.findById(id);
            return new CategoryResponseDTO(category.orElseThrow(() -> new ResourceNotFound(id)));
        }

        public CategoryResponseDTO update(Long id, CategoryRequestDTO categoryRequestDTO){
                Category category = categoryRepository.getReferenceById(id);
                updateData(category, categoryRequestDTO);
                categoryRepository.save(category);
                return new CategoryResponseDTO(category);
        }

    private void updateData(Category category, CategoryRequestDTO categoryRequestDTO) {
        category.setNameCategory(categoryRequestDTO.getNameCategory());
        category.setType(categoryRequestDTO.getType());
    }

    public void delete(Long id){

        if(!categoryRepository.existsById(id)){
            throw new ResourceNotFound(id);
        }
        try{
            categoryRepository.deleteById(id);
            // Resolver exceção
        } catch (InvalidDataAccessApiUsageException e) {
            throw new DataBase("This release is associated with a category");
        }
    }

}
