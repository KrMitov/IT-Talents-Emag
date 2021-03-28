package com.emag.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_carts")
public class UserCart{
    @EmbeddedId
    private UserCartsKey primaryKey;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;
    private int quantity;

}
