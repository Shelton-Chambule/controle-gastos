package com.chambule.controle_gastos.dto.user;
import com.chambule.controle_gastos.entities.User;
import java.time.LocalDate;
public class UserResponseDTO {

    private Long userId;
    private String email;
    private LocalDate creationDate;

    public UserResponseDTO(){}

    public UserResponseDTO(User user) {
        this.userId = user.getId();
        this.email = user.getLogin();
        this.creationDate = user.getCreationDate();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
