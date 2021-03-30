package com.emag.model.dto.userdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestUserDTO {
    private String email;
    private String password;
}