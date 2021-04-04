package com.emag.util;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dto.orderdto.CreateOrderDTO;
import com.emag.model.pojo.Coupon;
import com.emag.model.pojo.Product;
import com.emag.model.pojo.User;
import com.emag.model.pojo.UserCart;

import java.util.List;

public class OrderUtility {

    public static void setDiscountedPrice(Product product, int discountPercentage) {
        double regularPrice = product.getRegularPrice();
        double discountedPrice = regularPrice - regularPrice * discountPercentage / 100;
        product.setDiscountedPrice(discountedPrice);
    }

    public static void checkCartForProduct(User user, Product product) {
        boolean isFound = false;
        List<UserCart> productsInCart = user.getProductsInCart();
        for (UserCart userCart : productsInCart) {
            if (userCart.getProduct().getFullName().equals(product.getFullName())) {
                isFound = true;
                break;
            }
        }
        if (isFound == false) {
            throw new NotFoundException("Product not found in user's cart");
        }
    }

    public static int getQuantityOfProduct(User user, Product product) {
        int quantity = 0;
        List<UserCart> productsInCart = user.getProductsInCart();
        for (UserCart userCart : productsInCart) {
            if (userCart.getProduct().getFullName().equals(product.getFullName())) {
                quantity = userCart.getQuantity();
                break;
            }
        }
        return quantity;
    }

    public static void validateOrder(CreateOrderDTO dto) {
        if (dto.getProductsId().length == 0) {
            throw new BadRequestException("You have to select products to make an order");
        }
    }

    public static boolean couponIsValidForProduct(Coupon coupon, Product product) {
        boolean result = false;
        if (coupon.getProductHasCoupon() != null) {
            if (coupon.getProductHasCoupon().getFullName().equals(product.getFullName())) {
                result = true;
            }
        } else {
            if (coupon.getCategory() != null) {
                if (coupon.getCategory().getName().equals(product.getCategory().getName())) {
                    result = true;
                }
            }
        }
        return result;
    }

    public static int getDiscountPercentage(CreateOrderDTO dto) {
        if (dto.getCoupon().getDiscountPercent() == 0) {
            throw new BadRequestException("Invalid coupon");
        }
        return dto.getCoupon().getDiscountPercent();
    }

    public static boolean isCouponEmpty(CreateOrderDTO dto){
        boolean result = false;
        if(dto.getCoupon().getDiscountPercent() == 0 && dto.getCoupon().getProductId() == 0
                && dto.getCoupon().getCategoryId() == 0 && dto.getCoupon().getStartDate().length() == 0
                && dto.getCoupon().getExpireDate().length() ==0){
            result = true;
        }
        return result;
    }

}
