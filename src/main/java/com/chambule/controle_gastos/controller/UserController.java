package com.chambule.controle_gastos.controller;
import com.chambule.controle_gastos.dto.token.TokenResponse;
import com.chambule.controle_gastos.dto.user.UserRequest;
import com.chambule.controle_gastos.dto.user.UserResponse;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.services.TokenService;
import com.chambule.controle_gastos.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/save")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequestDTO) {
        log.info("POST /users/save - email='{}'", userRequestDTO.getLogin());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userRequestDTO));
    }

    @PostMapping("/save/admin")
    public ResponseEntity<UserResponse> createAdmin(@Valid @RequestBody UserRequest userRequestDTO) {
        log.info("POST /users/save/admin - email='{}'", userRequestDTO.getLogin());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveAdmin(userRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserRequest userRequest) {
        log.info("POST /users/login - email='{}'", userRequest.getLogin());
        var username = new UsernamePasswordAuthenticationToken(userRequest.getLogin(), userRequest.getPassword());
        try {
            var authentication = authenticationManager.authenticate(username);
            log.info("Login successful for email='{}'", userRequest.getLogin());
            return ResponseEntity.status(HttpStatus.OK).body(new TokenResponse(tokenService.generateToken((User) authentication.getPrincipal())));
        } catch (Exception e) {
            log.warn("Login failed for email='{}': {}", userRequest.getLogin(), e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> update(@PathVariable Long userId, @Valid @RequestBody UserRequest userRequestDTO, Authentication authentication) {
        log.info("PUT /users/{}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(userId, userRequestDTO, authentication));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserResponse>> getAllUsers(Pageable pageable) {
        log.info("GET /users/getAll");
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(pageable));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getOneUser(@PathVariable Long userId, Authentication authentication) {
        log.info("GET /users/{}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId, authentication));
    }
}