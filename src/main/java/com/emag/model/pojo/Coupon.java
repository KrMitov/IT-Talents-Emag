package com.emag.model.pojo;

import com.emag.model.dto.coupondto.CouponDTO;
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
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int discountPercent;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "product_id")
    private Product productHasCoupon;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "category_id")
    private Category category;
    private Timestamp startDate;
    private Timestamp expireDate;
    @OneToMany(mappedBy = "coupon")
    @JsonBackReference
    private List<Order> ordersHaveCoupon;

    public Coupon(CouponDTO dto,Product product,Category category){
        this.discountPercent = dto.getDiscountPercent();
        this.productHasCoupon = product;
        this.category = category;
        this.startDate = Timestamp.valueOf(dto.getStartDate());
        if(dto.getExpireDate().length()>0) {
            this.expireDate = Timestamp.valueOf(dto.getExpireDate());
        }
    }

}
