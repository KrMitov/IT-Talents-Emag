package com.emag.model.dto.userdto;

import com.emag.model.dto.produtcdto.ProductDTO;
import com.emag.model.pojo.Product;
import com.emag.model.pojo.UserCart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCartDTO {

    private ProductDTO product;
    private int quantity;

    public UserCartDTO(UserCart userCart) {
        this.product = new ProductDTO(userCart.getProduct());
        this.quantity = userCart.getQuantity();
    }
}
