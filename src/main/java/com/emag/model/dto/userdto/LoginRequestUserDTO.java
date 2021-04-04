package com.emag.model.dto.userdto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequestUserDTO {
    @NotNull(message = "You have to enter an email")
    @Size(min = 10,max = 25, message = "Incorrect email")
    private String email;
    @NotNull(message = "You have to enter a password")
    @Size(min = 8,max = 25, message = "Incorrect password")
    private String password;
}