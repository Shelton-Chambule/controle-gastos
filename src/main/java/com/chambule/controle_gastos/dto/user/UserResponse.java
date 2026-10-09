package com.chambule.controle_gastos.dto.user;
import com.chambule.controle_gastos.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class UserResponse {

    private Long userId;
    private String email;
    private LocalDate creationDate;

    public UserResponse(User user) {
        this.userId = user.getId();
        this.email = user.getLogin();
        this.creationDate = user.getCreationDate();
    }
}
