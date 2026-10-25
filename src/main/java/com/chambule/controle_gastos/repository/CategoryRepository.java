package com.chambule.controle_gastos.repository;
import com.chambule.controle_gastos.entities.Category;
import com.chambule.controle_gastos.entities.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, Long>  {

    Page<Category> findByUser_Id(Long userId, Pageable pageable);

    Optional<Category> findByIdAndUser_Id(Long categoryId, Long userId);

    Optional<Category> findByNameCategoryAndUser(@NotBlank(message = "Required field") String nameCategory, User user);
}
