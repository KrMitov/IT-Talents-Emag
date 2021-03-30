package com.emag.model.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id",insertable = false,updatable = false)
    private int userId;
    private Timestamp createdAt;
    @ManyToMany
    @JsonManagedReference
    @JoinTable(
            name = "orders_have_products",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")}
    )
    private List<Product> productsInOrder;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userHasOrder;
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
