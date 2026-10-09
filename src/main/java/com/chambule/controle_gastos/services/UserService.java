package com.chambule.controle_gastos.services;
import com.chambule.controle_gastos.dto.user.UserRequest;
import com.chambule.controle_gastos.dto.user.UserResponse;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.entities.enums.UserType;
import com.chambule.controle_gastos.repository.UserRepository;
import com.chambule.controle_gastos.exception.DuplicateEmail;
import com.chambule.controle_gastos.exception.ResourceNotFound;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService  implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(username);
        return user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserResponse save(UserRequest userRequestDTO) {

        if (userRepository.existsByLogin(userRequestDTO.getLogin())) {
            throw new DuplicateEmail("This email address is already registered");
        }

        User user = new User();
        user.setRole(UserType.USER);
        user.setLogin(userRequestDTO.getLogin());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        userRepository.save(user);
        return new UserResponse(user);

    }

    public UserResponse update(Long id, UserRequest userRequestDTO,Authentication authentication) {
        try {
            User user = userRepository.getReferenceById(id);
            validateUserAndAdmin(user,authentication);
            updateData(user, userRequestDTO);
            userRepository.save(user);
            return new UserResponse(user);
        } catch (EntityNotFoundException e) {
                throw new ResourceNotFound(id);
        }
    }

    private void updateData(User user, UserRequest userRequestDTO) {
            if (!user.getLogin().equals(userRequestDTO.getLogin())
                    && userRepository.existsByLogin(userRequestDTO.getLogin())) {
                throw new DuplicateEmail("This email address is already registered");
            }
            user.setLogin(userRequestDTO.getLogin());
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
    }

    public UserResponse findById(Long id,Authentication authentication){
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound(id));
        validateUser(user,authentication);
        return new UserResponse(user);
    }

    public List<UserResponse> findAll(){
        List<User> user = userRepository.findAll();
        return user.stream().map(UserResponse::new).collect(Collectors.toList());
    }

    private void validateUser(User user,Authentication authentication){
        boolean userAdmin = user.getLogin().equals(authentication.getName());
        if(!userAdmin) throw new AccessDeniedException("Access Denied!");

    }

    private  void  validateUserAndAdmin(User user, Authentication authentication){
        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));
        boolean users = user.getLogin().equals(authentication.getName());

        if (!admin && users) throw new AccessDeniedException("Access Denied");

    }
}
