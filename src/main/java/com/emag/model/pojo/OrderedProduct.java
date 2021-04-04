package com.emag.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders_have_products")
public class OrderedProduct {

    @EmbeddedId
    OrderedProductsKey primaryKey;
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    Order order;
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;
    @Column(name = "quantity")
    int quantity;

}
