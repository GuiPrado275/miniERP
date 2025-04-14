package com.projetominiERP.miniERP.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreateDTO {

    @NotBlank
    @Size(min = 3, max = 50)
    @Email(message = "O e-mail deve ser válido!")
    private String email;

    @NotBlank
    @Size(min = 6, max = 60)
    private String password;

}
