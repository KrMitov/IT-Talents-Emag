package com.emag.model.dto.registerDTO;


import com.emag.model.dto.roleDTO.RoleWithoutUsersDTO;
import com.emag.model.pojo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class RegisterResponseUserDTO {
    private int id;
    private String email;
    private String name;
    private RoleWithoutUsersDTO role;

    public RegisterResponseUserDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = new RoleWithoutUsersDTO(user.getRole());
    }
}
