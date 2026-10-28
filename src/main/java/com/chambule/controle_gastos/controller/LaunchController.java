package com.chambule.controle_gastos.controller;
import com.chambule.controle_gastos.dto.PageResponse;
import com.chambule.controle_gastos.dto.launch.BalanceResponse;
import com.chambule.controle_gastos.dto.launch.LaunchRequest;
import com.chambule.controle_gastos.dto.launch.LaunchResponse;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.services.LaunchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/launchs")
@RequiredArgsConstructor
public class LaunchController {

    private final LaunchService launchService;

    @PostMapping("/save")
    public ResponseEntity<LaunchResponse> createLaunch(@RequestBody @Valid LaunchRequest launchRequestDTO, @AuthenticationPrincipal User user) {
        log.info("POST /launchs/save - user id={}", user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(launchService.createLaunch(launchRequestDTO, user));
    }

    @DeleteMapping("/{launchId}")
    public ResponseEntity<Void> delete(@PathVariable Long launchId) {
        log.info("DELETE /launchs/{}", launchId);
        launchService.deleteById(launchId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find")
    public ResponseEntity<PageResponse<LaunchResponse>> getAllLaunchs(Pageable pageable) {
        log.info("GET /launchs/find");
        return ResponseEntity.status(HttpStatus.OK).body(launchService.findAll(pageable));
    }

    @GetMapping("/{launchId}")
    public ResponseEntity<LaunchResponse> getOneLaunch(@PathVariable Long launchId) {
        log.info("GET /launchs/{}", launchId);
        return ResponseEntity.status(HttpStatus.OK).body(launchService.findById(launchId));
    }

    @PutMapping("/{launchId}")
    public ResponseEntity<LaunchResponse> updateLaunch(@PathVariable Long launchId, @Valid @RequestBody LaunchRequest launchRequestDTO) {
        log.info("PUT /launchs/{}", launchId);
        return ResponseEntity.status(HttpStatus.OK).body(launchService.update(launchId, launchRequestDTO));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<LaunchResponse>> getOneLaunchByCategory(@PathVariable Long categoryId) {
        log.info("GET /launchs/category/{}", categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(launchService.findByCategoryId(categoryId));
    }

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> balanceTotal(Pageable pageable) {
        log.info("GET /launchs/balance");
        return ResponseEntity.status(HttpStatus.OK).body(launchService.findBalance(pageable));
    }
}