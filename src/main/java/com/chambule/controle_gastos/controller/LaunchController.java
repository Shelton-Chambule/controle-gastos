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

    @PostMapping("/save")
    public ResponseEntity<LaunchResponse> createLaunch(@Valid  @RequestBody LaunchRequest launchRequestDTO){
            return ResponseEntity.status(HttpStatus.CREATED).body(launchService.createLaunch(launchRequestDTO));
    }

    @DeleteMapping("/{launchId}")
    public ResponseEntity<Void> delete(@PathVariable Long launchId){
            launchService.deleteById(launchId);
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/find")
    public ResponseEntity<List<LaunchResponse>> getAllLaunchs(){
        return ResponseEntity.status(HttpStatus.OK).body(launchService.findAll());
    }

    @GetMapping("/{launchId}")
    public ResponseEntity<LaunchResponse> getOneLaunch(@PathVariable Long launchId){
        return  ResponseEntity.status(HttpStatus.OK).body( launchService.findById(launchId));
    }

    @PutMapping("/{launchId}")
    public ResponseEntity<LaunchResponse> update(@PathVariable Long launchId, @Valid @RequestBody LaunchRequest launchRequestDTO ){
        return  ResponseEntity.status(HttpStatus.OK).body( launchService.update(launchId,launchRequestDTO));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<List<LaunchResponse>> getOneLaunchByCategory(@PathVariable Long categoryId){
        return ResponseEntity.status(HttpStatus.OK).body( launchService.findByCategoryId(categoryId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BalanceResponse> balanceTotal(@PathVariable Long userId, Authentication authentication){
         return ResponseEntity.status(HttpStatus.OK).body( launchService.findBalance(userId,authentication));
    }
}
