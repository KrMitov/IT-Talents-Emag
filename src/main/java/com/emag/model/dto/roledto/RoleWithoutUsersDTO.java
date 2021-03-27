package com.emag.model.dto.roledto;

import com.emag.model.pojo.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class RoleWithoutUsersDTO {

    private int id;
    private String role_Type;

    public RoleWithoutUsersDTO(Role role){
        this.id = role.getId();
        this.role_Type = role.getRole_type();
    }
}
