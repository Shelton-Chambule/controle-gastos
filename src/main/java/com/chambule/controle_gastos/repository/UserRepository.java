package com.chambule.controle_gastos.repository;
import com.chambule.controle_gastos.entities.Category;
import com.chambule.controle_gastos.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository <User, Long > {


    boolean existsByLogin(String login);
    Optional<User> findByLogin(String login);
}
