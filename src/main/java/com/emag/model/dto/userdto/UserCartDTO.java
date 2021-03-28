package com.emag.model.dto.userdto;

import com.emag.model.pojo.Product;
import com.emag.model.pojo.UserCarts;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCartDTO {

    private Product product;
    private int quantity;

    public UserCartDTO(UserCarts userCart) {
        this.product = userCart.getProduct();
        this.quantity = userCart.getQuantity();
    }
}
