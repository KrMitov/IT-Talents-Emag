package com.emag.model.repository;

import com.emag.model.pojo.Category;
import com.emag.model.pojo.Coupon;
import com.emag.model.pojo.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Integer> {

    Coupon findByProductHasCoupon(Product productId);
    Coupon findByCategory(Category category);

}
