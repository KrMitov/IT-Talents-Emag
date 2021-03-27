package com.emag.model.dto;

import com.emag.model.pojo.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequestUserDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String name;
    @Autowired
    private RoleWithoutUsersDTO role;
}
