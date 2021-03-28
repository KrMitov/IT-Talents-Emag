package com.emag.model.dto.produtcdto;

import com.emag.model.dto.userdto.UserCartDTO;
import com.emag.model.pojo.User;
import com.emag.model.pojo.UserCart;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductsFromCartForUserDTO {
    private List<UserCartDTO> productsInCart;

    public ProductsFromCartForUserDTO(User user){
        this.productsInCart = new ArrayList<>();
        for (UserCart userCart : user.getProductsInCart()) {
            UserCartDTO dto = new UserCartDTO(userCart);
            this.productsInCart.add(dto);
        }
    }

}
