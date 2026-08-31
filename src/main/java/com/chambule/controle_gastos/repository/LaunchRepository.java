package com.chambule.controle_gastos.repository;
import com.chambule.controle_gastos.entities.Launch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LaunchRepository extends JpaRepository<Launch, Long> {

    List<Launch> findByUserIdAndCategoryId(Long userId, Long categoryId);

    List<Launch> findByUser_Id(Long Id);
}
