package com.emag.model.dto.orderdto;

import com.emag.model.dto.produtcdto.ProductDTO;
import com.emag.model.pojo.Order;
import com.emag.model.pojo.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {
    private List<ProductDTO> products;

    public OrderDTO(Order order){
        this.products = new ArrayList<>();
        for (Product product : order.getProductsInOrder()){
            ProductDTO dto = new ProductDTO(product);
            this.products.add(dto);
        }

    }
}
