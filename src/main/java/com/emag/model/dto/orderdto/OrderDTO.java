package com.emag.model.dto.orderdto;


import com.emag.model.dao.UserOrderDAO;
import com.emag.model.dto.produtcdto.ProductFromOrderDTO;
import com.emag.model.pojo.Order;
import com.emag.model.pojo.OrderedProduct;
import com.emag.model.pojo.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
public class OrderDTO {

    private Timestamp createdAt;
    private List<ProductFromOrderDTO> products;

    public OrderDTO(Order order) {
        this.createdAt = order.getCreatedAt();
        this.products = new ArrayList<>();
//        for (OrderedProduct product : order.getProductsQuantityInOrder()) {
//            ProductFromOrderDTO dto = new ProductFromOrderDTO(product.getProduct());
////            dto.setQuantity(product.getQuantity());
//            products.add(dto);
//        }
        for (Product product : order.getProductsInOrder()) {
            ProductFromOrderDTO dto = new ProductFromOrderDTO(product);
            this.products.add(dto);
        }
    }
}

