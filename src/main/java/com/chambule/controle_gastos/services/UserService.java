package com.chambule.controle_gastos.services;
import com.chambule.controle_gastos.dto.user.UserRequestDTO;
import com.chambule.controle_gastos.dto.user.UserResponseDTO;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.entities.enums.UserType;
import com.chambule.controle_gastos.repository.UserRepository;
import com.chambule.controle_gastos.services.exception.DuplicateEmail;
import com.chambule.controle_gastos.services.exception.ResourceNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService  implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO save(UserRequestDTO userRequestDTO) {
        User user = new User();

        if (userRepository.existsByLogin(userRequestDTO.getLogin())) {
            throw new DuplicateEmail("This email address is already registered");
        }
        user.setRole(UserType.USER);
        user.setLogin(userRequestDTO.getLogin());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        userRepository.save(user);
        return new UserResponseDTO(user);
    }

    public UserResponseDTO update(Long id, UserRequestDTO userRequestDTO) {
        try {
            User user = userRepository.getReferenceById(id);
            updateData(user, userRequestDTO);
            userRepository.save(user);
            return new UserResponseDTO(user);
        } catch (EntityNotFoundException e) {
                throw new ResourceNotFound(id);
        }
    }

    private void updateData(User user, UserRequestDTO userRequestDTO) {

            if (!user.getLogin().equals(userRequestDTO.getLogin())
                    && userRepository.existsByLogin(userRequestDTO.getLogin())) {
                throw new DuplicateEmail("This email address is already registered");
            }
            user.setLogin(userRequestDTO.getLogin());
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
    }

    public UserResponseDTO findById(Long id){
        Optional<User> user = userRepository.findById(id);
        return new UserResponseDTO(user.orElseThrow(() -> new ResourceNotFound(id)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(username);
        return user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
