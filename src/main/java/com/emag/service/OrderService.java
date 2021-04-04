package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dto.orderdto.CreateOrderDTO;
import com.emag.model.dto.orderdto.OrderConfirmationDTO;
import com.emag.model.dto.produtcdto.RequestProductDTO;
import com.emag.model.pojo.*;
import com.emag.util.OrderUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService extends AbstractService {

    @Autowired
    CartService cartService;
    @Autowired
    ProductService productService;

    @Transactional
    public OrderConfirmationDTO createOrder(CreateOrderDTO dto) throws SQLException {
        OrderUtility.validateOrder(dto);
        User user = getUserIfExists(dto.getUserId());
        Order order = new Order();
        order.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        order.setUserHasOrder(user);
        Coupon couponFromOrder = null;
        if (dto.getCouponId() != 0) {
            couponFromOrder = findCouponById(dto.getCouponId());
        }
        Product discountedProduct = null;
        Category discountedCategory = null;
        int discountPercentage = 0;
        if (couponFromOrder != null) {
            if (couponFromOrder.getProductHasCoupon() != null) {
                discountedProduct = couponFromOrder.getProductHasCoupon();
            }
            if (couponFromOrder.getCategory() != null) {
                discountedCategory = couponFromOrder.getCategory();
            }
            discountPercentage = couponFromOrder.getDiscountPercent();
        }
        int[] requestedProducts = dto.getProductsId();
        HashMap<Product, Integer> productQuantities = new HashMap<>();
        for (int i = 0; i < requestedProducts.length; i++) {
            Product product = getProductIfExists(requestedProducts[i]);
            OrderUtility.checkCartForProduct(user, product);
            int quantityOfProduct = OrderUtility.getQuantityOfProduct(user, product);
            productQuantities.put(product, quantityOfProduct);
            if (dto.getCouponId() != 0) {
                if (OrderUtility.couponIsValidForProduct(couponFromOrder, product)) {
                    changeProductPrice(product, discountedProduct, discountedCategory, discountPercentage);
                } else {
                    throw new BadRequestException("Coupon is not valid for " + product.getFullName());
                }
            }
            int productId = product.getId();
            cartService.removeProductFromCart(productId, user.getId());
            RequestProductDTO editedProduct = new RequestProductDTO();
            editedProduct.setQuantity(product.getQuantity() - quantityOfProduct);
            productService.editProduct(product.getId(), editedProduct);
        }
        orderRepository.save(order);
        insertOrderedQuantity(order, productQuantities);
        return new OrderConfirmationDTO("Order created successfully");
    }

    private void insertOrderedQuantity(Order order, HashMap<Product, Integer> productQuantities) {
        int orderId = order.getId();
        for (Map.Entry<Product, Integer> entry : productQuantities.entrySet()) {
            Product product = entry.getKey();
            OrderedProductsKey primaryKey = new OrderedProductsKey();
            primaryKey.setOrderId(orderId);
            primaryKey.setProductId(product.getId());
            OrderedProduct orderedProduct = new OrderedProduct();
            orderedProduct.setPrimaryKey(primaryKey);
            orderedProduct.setOrder(order);
            orderedProduct.setProduct(product);
            orderedProduct.setQuantity(entry.getValue());
            orderedProductsRepository.save(orderedProduct);
        }
    }

    private void changeProductPrice(Product product, Product discountedProduct, Category discountedCategory, int discountPercentage) {
        if (discountedProduct != null) {
            if (product.getFullName().equals(discountedProduct.getFullName())) {
                OrderUtility.setDiscountedPrice(product, discountPercentage);
            } else {
                throw new BadRequestException("The entered coupon is not valid");
            }
        }else {
            if (discountedCategory != null) {
                if (product.getCategory().getName().equals(discountedCategory.getName())) {
                    OrderUtility.setDiscountedPrice(product, discountPercentage);
                } else {
                    throw new BadRequestException("The entered coupon is not valid");
                }
            }else {
                throw new BadRequestException("The entered coupon is not valid");
            }
        }
    }

}
