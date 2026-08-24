package com.chambule.controle_gastos.dto.user;
import com.chambule.controle_gastos.entities.User;
import java.time.LocalDate;

public class UserResponseDTO {

    private Long userId;
    private String name;
    private String email;
    private LocalDate creation_date;

    public UserResponseDTO(){}

    public UserResponseDTO(User user) {
        this.userId = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.creation_date = user.getCreation_date();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(LocalDate creation_date) {
        this.creation_date = creation_date;
    }
}
