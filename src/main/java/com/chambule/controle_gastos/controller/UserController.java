package com.chambule.controle_gastos.controller;
import com.chambule.controle_gastos.dto.user.UserRequestDTO;
import com.chambule.controle_gastos.dto.user.UserResponseDTO;
import com.chambule.controle_gastos.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/user/save")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                );

        return http.build();
    }

    @PostMapping("/save")
    public ResponseEntity<UserResponseDTO> save(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO use = userService.save(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(use);
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO userResponseDTO = userService.update(id,userRequestDTO);
        return ResponseEntity.ok().body(userResponseDTO);
    }

}
