package com.emag.service;

import com.emag.model.dto.coupondto.CouponDTO;
import com.emag.model.pojo.Category;
import com.emag.model.pojo.Coupon;
import com.emag.model.pojo.Product;
import org.springframework.stereotype.Service;

@Service
public class CouponService extends AbstractService{

    public String createCoupon(CouponDTO dto){
        Product product = productRepository.findById(dto.getProductId()).get();
        Category category = categoryRepository.findById(dto.getCategoryId()).get();
        Coupon coupon = new Coupon(dto,product,category);
        couponRepository.save(coupon);
        return "Coupon created successfully";
    }

}
