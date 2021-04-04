package com.emag.model.dto.registerdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequestUserDTO {
    @NotNull(message = "Email must not be null")
    @Size(min=12,max=25,message = "Incorrect email")
    private String email;
    @NotNull(message = "Password must not be null")
    @Size(min=8,max=25,message = "Incorrect password length")
    private String password;
    @NotNull(message = "Password must not be null")
    @Size(min=8,max=25,message = "Incorrect password length")
    private String confirmPassword;
    @NotNull(message = "Name must not be null")
    @Size(min=3,max=35,message = "Incorrect name")
    private String name;

}
