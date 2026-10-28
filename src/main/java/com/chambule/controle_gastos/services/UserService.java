package com.chambule.controle_gastos.services;
import com.chambule.controle_gastos.dto.user.UserRequest;
import com.chambule.controle_gastos.dto.user.UserResponse;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.mapper.UserMapper;
import com.chambule.controle_gastos.entities.enums.UserType;
import com.chambule.controle_gastos.repository.UserRepository;
import com.chambule.controle_gastos.exception.DuplicateEmail;
import com.chambule.controle_gastos.exception.ResourceNotFound;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username='{}'", username);
        Optional<User> user = userRepository.findByLogin(username);
        return user.orElseThrow(() -> {
            log.warn("Username='{}' not found", username);
            return new UsernameNotFoundException("User not found");
        });
    }

    public UserResponse saveAdmin(UserRequest userRequestDTO) {
        log.info("Creating new admin, email='{}'", userRequestDTO.getLogin());
        if (userRepository.existsByLogin(userRequestDTO.getLogin())) {
            log.warn("Admin creation rejected: email='{}' already registered", userRequestDTO.getLogin());
            throw new DuplicateEmail("This email address is already registered");
        }

        User user = new User();
        user.setRole(UserType.ADMIN);
        user.setLogin(userRequestDTO.getLogin());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        userRepository.save(user);
        log.info("Admin created successfully, id={}, email='{}'", user.getId(), user.getLogin());
        return new UserResponse(user);
    }

    public UserResponse saveUser(UserRequest userRequestDTO) {
        log.info("Creating new user, email='{}'", userRequestDTO.getLogin());
        if (userRepository.existsByLogin(userRequestDTO.getLogin())) {
            log.warn("User creation rejected: email='{}' already registered", userRequestDTO.getLogin());
            throw new DuplicateEmail("This email address is already registered");
        }

        User user = new User();
        user.setRole(UserType.USER);
        user.setLogin(userRequestDTO.getLogin());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        userRepository.save(user);
        log.info("User created successfully, id={}, email='{}'", user.getId(), user.getLogin());
        return new UserResponse(user);
    }

    public UserResponse update(Long id, UserRequest userRequestDTO, Authentication authentication) {
        log.info("Updating user id={}", id);
        try {
            User user = userRepository.getReferenceById(id);
            validateUserAndAdmin(user, authentication);
            updateData(user, userRequestDTO);
            userRepository.save(user);
            log.info("User id={} updated successfully", id);
            return new UserResponse(user);
        } catch (EntityNotFoundException e) {
            log.error("Failed to update user id={}: not found", id);
            throw new ResourceNotFound(id);
        }
    }

    private void updateData(User user, UserRequest userRequestDTO) {
        if (!user.getLogin().equals(userRequestDTO.getLogin())
                && userRepository.existsByLogin(userRequestDTO.getLogin())) {
            log.warn("User update rejected: email='{}' already registered", userRequestDTO.getLogin());
            throw new DuplicateEmail("This email address is already registered");
        }
        user.setLogin(userRequestDTO.getLogin());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
    }

    public UserResponse findById(Long id, Authentication authentication) {
        log.debug("Fetching user by id={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User id={} not found", id);
                    return new ResourceNotFound(id);
                });
        validateUser(user, authentication);
        return new UserResponse(user);
    }

    public List<UserResponse> findAll(Pageable pageable) {
        log.info("Listing users, page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<User> page = userRepository.findAll(pageable);
        List<UserResponse> users = page.map(UserMapper::toResponse).getContent();
        log.info("Returning {} user(s)", users.size());
        return users;
    }

    private void validateUser(User user, Authentication authentication) {
        boolean userAdmin = user.getLogin().equals(authentication.getName());
        if (!userAdmin) {
            log.warn("Access denied: login='{}' tried to access user id={}", authentication.getName(), user.getId());
            throw new AccessDeniedException("Access Denied!");
        }
    }

    private void validateUserAndAdmin(User user, Authentication authentication) {
        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));
        boolean users = user.getLogin().equals(authentication.getName());

        if (!admin && users) {
            log.warn("Access denied: login='{}' tried to update user id={}", authentication.getName(), user.getId());
            throw new AccessDeniedException("Access Denied");
        }
    }
}