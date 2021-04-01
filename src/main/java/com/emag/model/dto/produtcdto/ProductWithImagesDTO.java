package com.emag.model.dto.produtcdto;

import com.emag.model.pojo.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductWithImagesDTO {

    private int productId;
    private List<Integer> imagesIds;

    public ProductWithImagesDTO(Product product) {
        this.productId = product.getId();
        this.imagesIds = new ArrayList<>();
        product.getProductImages().forEach(image -> imagesIds.add(image.getId()));
    }
}
