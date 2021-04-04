package com.emag.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User userHasOrder;
    private Timestamp createdAt;
    @OneToMany(mappedBy = "order")
    private List<OrderedProduct> productsQuantityInOrder;
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
