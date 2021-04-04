package com.emag.model.dto.userdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LogoutDTO {
    private String message;

    public LogoutDTO(String message){
        this.message = message;
    }
}
