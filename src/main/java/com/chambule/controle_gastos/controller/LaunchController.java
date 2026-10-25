package com.chambule.controle_gastos.controller;
import com.chambule.controle_gastos.dto.PageResponse;
import com.chambule.controle_gastos.dto.launch.BalanceResponse;
import com.chambule.controle_gastos.dto.launch.LaunchRequest;
import com.chambule.controle_gastos.dto.launch.LaunchResponse;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.services.LaunchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/launchs")
@RequiredArgsConstructor
public class LaunchController {

    private final LaunchService launchService;

    @PostMapping("/save")
    public ResponseEntity<LaunchResponse> createLaunch( @RequestBody @Valid LaunchRequest launchRequestDTO, @AuthenticationPrincipal User user){
            return ResponseEntity.status(HttpStatus.CREATED).body(launchService.createLaunch(launchRequestDTO,user));
    }

    @DeleteMapping("/{launchId}")
    public ResponseEntity<Void> delete(@PathVariable Long launchId){
            launchService.deleteById(launchId);
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/find")
    public ResponseEntity<PageResponse<LaunchResponse>> getAllLaunchs(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(launchService.findAll(pageable));
    }

    @GetMapping("/getOne/{launchId}")
    public ResponseEntity<LaunchResponse> getOneLaunch(@PathVariable Long launchId){
        return  ResponseEntity.status(HttpStatus.OK).body( launchService.findById(launchId));
    }

    @PutMapping("/{launchId}")
    public ResponseEntity<LaunchResponse> updateLaunch(@PathVariable Long launchId, @Valid @RequestBody LaunchRequest launchRequestDTO ){
        return  ResponseEntity.status(HttpStatus.OK).body( launchService.update(launchId,launchRequestDTO));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<LaunchResponse>> getOneLaunchByCategory(@PathVariable Long categoryId){
        return ResponseEntity.status(HttpStatus.OK).body( launchService.findByCategoryId(categoryId));
    }

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> balanceTotal(Pageable pageable){
         return ResponseEntity.status(HttpStatus.OK).body( launchService.findBalance(pageable));
    }
}
