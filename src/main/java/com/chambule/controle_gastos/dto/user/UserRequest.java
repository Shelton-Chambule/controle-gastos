package com.chambule.controle_gastos.dto.user;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserRequest {

    @JsonProperty(required = true)
    @NotBlank(message = "Campo obrigatório")
    @Email(message = "formato do email, invalido!")
    private String login;

    @NotBlank(message = "Campo obrigatório")
    @JsonProperty(required = true)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "A senha deve conter maiúscula, minúscula, número e caractere especial")
    private String password;
}
