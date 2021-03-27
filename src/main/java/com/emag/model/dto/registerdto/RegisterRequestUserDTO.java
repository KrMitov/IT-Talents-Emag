package com.emag.model.dto.registerdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequestUserDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String name;

}
