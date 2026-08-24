package com.chambule.controle_gastos.repository;
import com.chambule.controle_gastos.entities.Launch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaunchRepository  extends JpaRepository <Launch, Long> {
}
