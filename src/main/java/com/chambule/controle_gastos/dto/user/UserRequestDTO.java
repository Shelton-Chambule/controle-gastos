package com.chambule.controle_gastos.dto.user;
import com.chambule.controle_gastos.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
public class UserRequestDTO {

    // Dados enviados pelo usuário
    @NotBlank(message = "Nome e obrigatório")
    private String name;

    @NotBlank(message = "Campo obrigatório")
    @Email(message = "formato do email, invalido!")
    private String email;

    @NotBlank(message = "Campo obrigatório")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "A senha deve conter maiúscula, minúscula, número e caractere especial")
    private String password;

    public UserRequestDTO(){}

    public UserRequestDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
