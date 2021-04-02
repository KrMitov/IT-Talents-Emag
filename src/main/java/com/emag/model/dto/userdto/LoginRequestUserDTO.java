package com.emag.model.dto.userdto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequestUserDTO {
    @NotBlank(message = "you have to enter email")
    private String email;
    private String password;
}