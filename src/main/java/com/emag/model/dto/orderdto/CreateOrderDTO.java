package com.emag.model.dto.orderdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateOrderDTO {
    private int[] productsId;
    private int userId;
}
