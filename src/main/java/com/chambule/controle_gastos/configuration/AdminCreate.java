package com.chambule.controle_gastos.configuration;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.entities.enums.UserType;
import com.chambule.controle_gastos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class AdminCreate implements CommandLineRunner {

    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
                if (!userRepository.existsByLogin("admin2@gmail.com")){
                    User admin = new User();
                    admin.setLogin("admin2@gmail.com");
                    admin.setPassword(passwordEncoder.encode("Chambule123@#$"));
                    admin.setRole(UserType.ADMIN);
                    admin.setCreationDate(LocalDate.of(2026,10,24));

                    userRepository.save(admin);

                }
    }
}
