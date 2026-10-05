package com.chambule.controle_gastos.dto.user;
import com.chambule.controle_gastos.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserRequestDTO {

    @NotBlank(message = "Campo obrigatório")
    @Email(message = "formato do email, invalido!")
    private String login;

    @NotBlank(message = "Campo obrigatório")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "A senha deve conter maiúscula, minúscula, número e caractere especial")
    private String password;

    public UserRequestDTO(){}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
