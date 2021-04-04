package com.emag.model.dto.orderdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderConfirmationDTO{

    private String message;

    public OrderConfirmationDTO(String message){
        this.message = message;
    }

}
