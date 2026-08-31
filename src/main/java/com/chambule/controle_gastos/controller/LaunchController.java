package com.chambule.controle_gastos.controller;
import com.chambule.controle_gastos.dto.launch.BalanceResponseDTO;
import com.chambule.controle_gastos.dto.launch.LaunchRequestDTO;
import com.chambule.controle_gastos.dto.launch.LaunchResponseDTO;
import com.chambule.controle_gastos.services.LaunchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/launch")
public class LaunchController {

    private final LaunchService launchService;

    public LaunchController(LaunchService launchService) {
        this.launchService = launchService;
    }

    @PostMapping("/createLaunch")
    public ResponseEntity<LaunchResponseDTO> createLaunch(@RequestBody LaunchRequestDTO launchRequestDTO){
            LaunchResponseDTO launch = launchService.createLaunch(launchRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(launch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
            launchService.deleteById(id);
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/find")
    public ResponseEntity<List<LaunchResponseDTO>> findAll(){
        List<LaunchResponseDTO> launch = launchService.findAll();
        return ResponseEntity.ok().body(launch);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaunchResponseDTO> findById(@PathVariable Long id){
        LaunchResponseDTO launch = launchService.findById(id);
        return  ResponseEntity.ok().body(launch);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<LaunchResponseDTO> update(@PathVariable Long id, @RequestBody LaunchRequestDTO launchRequestDTO ){
        LaunchResponseDTO launch = launchService.update(id,launchRequestDTO);
        return  ResponseEntity.ok().body(launch);
    }

    @GetMapping("/{userId}/{categoryId}")
    public ResponseEntity<List<LaunchResponseDTO>> findByUser_Id_Category_Id(@PathVariable Long userId, @PathVariable   Long categoryId){
        List<LaunchResponseDTO> launch = launchService.findByUserIdAndCategoryId(userId,categoryId);
        return ResponseEntity.ok().body(launch);
    }

    @GetMapping("/{userId}/balance")
    public ResponseEntity<BalanceResponseDTO> balanceTotal(@PathVariable  Long userId){
         BalanceResponseDTO balance = launchService.findByUser_Id(userId);
         return ResponseEntity.ok(balance);
    }
}
