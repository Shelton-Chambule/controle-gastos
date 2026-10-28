package com.chambule.controle_gastos.repository;
import com.chambule.controle_gastos.entities.Launch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LaunchRepository extends JpaRepository<Launch, Long> {

    List<Launch> findByUser_IdAndCategory_Id(Long userId, Long categoryId);

    List<Launch> findByCategory_Id(Long categoryId);

    Page<Launch> findByUser_Id(Long userId, Pageable pageable);

    Optional<Launch> findByIdAndUser_Id(Long launchId, Long userId);
}
