package com.emag.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestUserDTO {

    private String email;
    private String password;

}