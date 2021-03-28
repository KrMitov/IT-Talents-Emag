package com.emag.model.dto.produtcdto;

import com.emag.model.pojo.Product;
import com.emag.model.pojo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LikedProductsForUserDTO {
    private List<ProductDTO> likedProducts;

    public LikedProductsForUserDTO(User user){
        this.likedProducts = new ArrayList<>();
        for (Product likedProduct : user.getLikedProducts()) {
            likedProducts.add(new ProductDTO(likedProduct));
        }
    }
}
