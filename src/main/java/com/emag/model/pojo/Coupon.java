package com.emag.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "product_id")
    private Product productHasCoupon;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "category_id")
    private Category category;
    private int startDate;
    private int expireDate;
    @OneToMany(mappedBy = "coupon")
    @JsonBackReference
    private List<Order> ordersHaveCoupon;
}
