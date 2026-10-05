package com.chambule.controle_gastos.controller;
import com.chambule.controle_gastos.dto.user.UserRequestDTO;
import com.chambule.controle_gastos.dto.user.UserResponseDTO;
import com.chambule.controle_gastos.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    public ResponseEntity<UserResponseDTO> save(@Valid  @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO use = userService.save(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(use);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.update(id, userRequestDTO);
        return ResponseEntity.ok().body(userResponseDTO);
    }
}
