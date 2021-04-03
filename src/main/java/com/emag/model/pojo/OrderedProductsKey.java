package com.emag.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class OrderedProductsKey implements Serializable {

    @Column(name = "order_id")
    int orderId;
    @Column(name = "product_id")
    int productId;

}
