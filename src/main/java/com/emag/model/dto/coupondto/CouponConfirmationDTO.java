package com.emag.model.dto.coupondto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CouponConfirmationDTO {

    private String message;

    public CouponConfirmationDTO(String message){
        this.message = message;
    }

}
