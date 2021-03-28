package com.emag.model.dto.cartdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCartQuantityDTO {
    private int userId;
    private int productId;
    private int quantity;
}
