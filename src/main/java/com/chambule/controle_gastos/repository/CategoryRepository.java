package com.chambule.controle_gastos.repository;
import com.chambule.controle_gastos.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, Long>  {
}
