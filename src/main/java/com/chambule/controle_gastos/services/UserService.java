package com.chambule.controle_gastos.services;
import com.chambule.controle_gastos.dto.user.UserRequestDTO;
import com.chambule.controle_gastos.dto.user.UserResponseDTO;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.repository.UserRepository;
import com.chambule.controle_gastos.services.exception.DuplicateEmail;
import com.chambule.controle_gastos.services.exception.ResourceNotFound;
import jakarta.persistence.EntityNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
   // private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
       // this.encoder = encoder;
    }

    public UserResponseDTO save(UserRequestDTO userRequestDTO) {

        User user = new User();

        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new DuplicateEmail("Esse email ja se encontra cadastrado");
        }
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setCreationDate(LocalDate.now());
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
            user.setName(userRequestDTO.getName());
            user.setEmail(userRequestDTO.getEmail());
            user.setPassword(userRequestDTO.getPassword());
    }

    public UserResponseDTO findById(Long id){
        Optional<User> user = userRepository.findById(id);
        return new UserResponseDTO(user.orElseThrow(() -> new ResourceNotFound(id)));
    }
}
