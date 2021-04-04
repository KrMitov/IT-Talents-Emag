package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.model.dto.coupondto.CouponConfirmationDTO;
import com.emag.model.dto.coupondto.CouponDTO;
import com.emag.model.pojo.Category;
import com.emag.model.pojo.Coupon;
import com.emag.model.pojo.Product;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class CouponService extends AbstractService{

    public CouponConfirmationDTO createCoupon(CouponDTO dto){
        this.validateCoupon(dto);
        Coupon coupon = new Coupon(dto);
        if(dto.getStartDate()!=null && dto.getStartDate().length()>0){
            LocalDateTime startDate = validateDate(dto.getStartDate());
            coupon.setStartDate(Timestamp.valueOf(startDate));
        }else{
            LocalDateTime expireDate = validateDate(dto.getExpireDate());
            coupon.setExpireDate(Timestamp.valueOf(expireDate));
        }
        if(dto.getProductId()!=0) {
            Product product = getProductIfExists(dto.getProductId());
            coupon.setProductHasCoupon(product);
        }
        if(dto.getCategoryId() != 0) {
            Category category = getCategoryIfExists(dto.getCategoryId());
            coupon.setCategory(category);
        }
        couponRepository.save(coupon);
        return new CouponConfirmationDTO("Coupon created successfully");
    }

    private void validateCoupon(CouponDTO dto){
        if(dto.getDiscountPercent() == null){
            throw new BadRequestException("Enter a valid discount percent");
        }else {
            if (dto.getDiscountPercent() == 0){
                throw new BadRequestException("Enter a valid discount percent");
            }
        }
        if(dto.getProductId() ==0 && dto.getCategoryId() == 0){
            throw new BadRequestException("Enter product id or category id");
        }else{
            if(dto.getProductId() == null && dto.getCategoryId() == null){
                throw new BadRequestException("Enter product id or category id");
            }
        }
        LocalDateTime startDate;
        if(dto.getStartDate() == null){
            throw new BadRequestException("Enter a valid start date");
        }else{
            startDate = validateDate(dto.getStartDate());
        }
        if(dto.getExpireDate()!=null && dto.getExpireDate().length()>0){
            LocalDateTime endDate = validateDate(dto.getExpireDate());
            if (endDate.isBefore(startDate)){
                throw new BadRequestException("Enter a valid expiration date");
            }
        }
    }

}
