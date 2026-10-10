package com.chambule.controle_gastos.controller;
import com.chambule.controle_gastos.dto.TokenResponse;
import com.chambule.controle_gastos.dto.user.UserRequest;
import com.chambule.controle_gastos.dto.user.UserResponse;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.services.TokenService;
import com.chambule.controle_gastos.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/save")
    public ResponseEntity<UserResponse> createUser(@Valid  @RequestBody UserRequest userRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login (@RequestBody UserRequest userRequest){
            var  username = new UsernamePasswordAuthenticationToken(userRequest.getLogin() , userRequest.getPassword());
            var  authentication = authenticationManager.authenticate(username);
            return  ResponseEntity.status(HttpStatus.OK).body(new TokenResponse(tokenService.generateToken( (User) authentication.getPrincipal())));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> update(@PathVariable Long userId, @Valid @RequestBody UserRequest userRequestDTO, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(userId, userRequestDTO,authentication));
    }

    @GetMapping("/getAll")
    public  ResponseEntity<List<UserResponse>> getAllUsers(){
            return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{userId}")
    public  ResponseEntity<UserResponse> getOneUser(@PathVariable  Long userId, Authentication authentication){
        return  ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId, authentication));
    }
}
