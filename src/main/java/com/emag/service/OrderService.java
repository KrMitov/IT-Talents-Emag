package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dto.orderdto.CreateOrderDTO;
import com.emag.model.dto.orderdto.OrderConfirmationDTO;
import com.emag.model.dto.produtcdto.RequestProductDTO;
import com.emag.model.pojo.*;
import com.emag.model.repository.*;
import com.emag.util.OrderUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Array;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService extends AbstractService{

    private static final int MIN_PERCENTAGE_VALUE = 1;
    private static final int MAX_PERCENTAGE_VALUE = 95;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    CartService cartService;
    @Autowired
    OrderedProductsRepository orderedProductsRepository;
    @Autowired
    ProductService productService;

    @Transactional
    public OrderConfirmationDTO createOrder(CreateOrderDTO dto){
        OrderUtility.validateOrder(dto);
        User user = getUserIfExists(dto.getUserId());
        Order order = new Order();
        order.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        order.setUserHasOrder(user);
        Product discountedProduct = null;
        if(dto.getCoupon().getProductId()!=0) {
            discountedProduct = this.getProductIfExists(dto.getCoupon().getProductId());
        }
        Category discountedCategory = null;
        if(dto.getCoupon().getCategoryId()!=0){
            discountedCategory = getCategoryIfExists(dto.getCoupon().getCategoryId());
        }
        int discountPercentage = dto.getCoupon().getDiscountPercent();
        int[] requestedProducts = dto.getProductsId();
        HashMap<Product,Integer> productQuantities = new HashMap<>();
        for (int i = 0; i < requestedProducts.length; i++) {
            Product product = getProductIfExists(requestedProducts[i]);
            OrderUtility.checkCartForProduct(user, product);
            int quantityOfProduct = OrderUtility.getQuantityOfProduct(user, product);
            productQuantities.put(product, quantityOfProduct);
            if (!OrderUtility.isCouponEmpty(dto)) {
                Coupon coupon = this.validateCoupon(dto);
                if (OrderUtility.couponIsValidForProduct(coupon, product)) {
                    changeProductPrice(product, discountedProduct, discountedCategory, discountPercentage);
                }else{
                    throw new BadRequestException("Coupon is not valid for "+product.getFullName());
                }
            }
            int productId = product.getId();
            cartService.removeProductFromCart(productId, user.getId());
            RequestProductDTO editedProduct = new RequestProductDTO();
            editedProduct.setQuantity(product.getQuantity() - quantityOfProduct);
            productService.editProduct(product.getId(), editedProduct);
        }
        orderRepository.save(order);
        insertOrderedQuantity(order,productQuantities);
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

    private Coupon validateCoupon(CreateOrderDTO dto){
        Coupon coupon = null;
        if(!OrderUtility.isCouponEmpty(dto)){
            if(dto.getCoupon().getProductId() == 0 && dto.getCoupon().getCategoryId() == 0){
                throw new BadRequestException("Invalid coupon");
            }
            if(dto.getCoupon().getProductId()!=0){
                coupon = couponRepository.findByProductHasCoupon(productRepository.findById(dto.getCoupon().getProductId()).get());
            }else{
                coupon = couponRepository.findByCategory(categoryRepository.findById(dto.getCoupon().getCategoryId()).get());
            }
            if(coupon == null){
                throw new NotFoundException("The coupon you entered does not exist");
            }
            if(coupon.getDiscountPercent() != dto.getCoupon().getDiscountPercent()){
                throw new BadRequestException("Invalid coupon") ;
            }
            int discountPercent = OrderUtility.getDiscountPercentage(dto);
            if (discountPercent < MIN_PERCENTAGE_VALUE || discountPercent > MAX_PERCENTAGE_VALUE) {
                throw new BadRequestException("Invalid coupon");
            }
            if (dto.getCoupon().getStartDate() != null) {
                LocalDateTime startDate = this.validateDate(dto.getCoupon().getStartDate());
                if (LocalDateTime.now().isBefore(startDate)) {
                    throw new BadRequestException("Invalid coupon");
                }
            }else{
                throw new BadRequestException("Invalid coupon");
            }
            if (dto.getCoupon().getExpireDate() != null) {
                LocalDateTime endDate = this.validateDate(dto.getCoupon().getExpireDate());
                if (LocalDateTime.now().isAfter(endDate)) {
                    throw new BadRequestException("Invalid coupon");
                }
            }
        }
        return coupon;
    }

    private void changeProductPrice(Product product, Product discountedProduct, Category discountedCategory, int discountPercentage){
        if(discountedProduct != null) {
            if(product.getFullName().equals(discountedProduct.getFullName())) {
                OrderUtility.setDiscountedPrice(product, discountPercentage);
            }else{
                throw new BadRequestException("The entered coupon is not valid");
            }
        }else{
            if(discountedCategory != null){
                if(product.getCategory().getName().equals(discountedCategory.getName())) {
                    OrderUtility.setDiscountedPrice(product, discountPercentage);
                } else {
                    throw new BadRequestException("The entered coupon is not valid");
                }
            } else {
                throw new BadRequestException("The entered coupon is not valid");
            }
        }
    }

}
