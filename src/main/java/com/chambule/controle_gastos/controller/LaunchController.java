package com.chambule.controle_gastos.controller;
import com.chambule.controle_gastos.dto.launch.BalanceResponse;
import com.chambule.controle_gastos.dto.launch.LaunchRequest;
import com.chambule.controle_gastos.dto.launch.LaunchResponse;
import com.chambule.controle_gastos.services.LaunchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/launchs")
@RequiredArgsConstructor
public class LaunchController {

    private final LaunchService launchService;

    @PostMapping("/createLaunch")
    public ResponseEntity<LaunchResponse> createLaunch(@Valid  @RequestBody LaunchRequest launchRequestDTO, Authentication authentication){
            return ResponseEntity.status(HttpStatus.CREATED).body(launchService.createLaunch(launchRequestDTO,authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,Authentication authentication){
            launchService.deleteById(id,authentication);
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/find")
    public ResponseEntity<List<LaunchResponse>> getAllLaunchs(){
        return ResponseEntity.ok().body(launchService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaunchResponse> findById(@PathVariable Long id){
        return  ResponseEntity.ok().body( launchService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaunchResponse> update(@PathVariable Long id, @Valid @RequestBody LaunchRequest launchRequestDTO ){
        return  ResponseEntity.ok().body( launchService.update(id,launchRequestDTO));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<LaunchResponse>> findByCategory(@PathVariable Long categoryId){
        List<LaunchResponse> launch = launchService.findByCategoryId(categoryId);
        return ResponseEntity.ok().body(launch);
    }

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> balanceTotal(){
         BalanceResponse balance = launchService.findBalance();
         return ResponseEntity.ok(balance);
    }
}
