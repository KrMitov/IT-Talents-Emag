package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dto.orderdto.CreateOrderDTO;
import com.emag.model.pojo.*;
import com.emag.model.repository.CategoryRepository;
import com.emag.model.repository.OrderRepository;
import com.emag.model.repository.ProductRepository;
import com.emag.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CartService cartService;
    private static final int MIN_PERCENTAGE_VALUE = 1;
    private static final int MAX_PERCENTAGE_VALUE = 95;

    @Transactional
    public String createOrder(CreateOrderDTO dto){
        this.validateVoucher(dto);
        User user = findUserById(dto.getUserId());
        Order order = new Order();
        order.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        order.setUserHasOrder(user);
        int[] requestedProducts = dto.getProductsId();
        Product discountedProduct = null;
        if(dto.getCoupon().getProductId()!=0) {
            discountedProduct = this.findProductById(dto.getCoupon().getProductId());
        }
        Category discountedCategory = null;
        if(dto.getCoupon().getCategoryId()!=0){
            discountedCategory = findCategoryById(dto.getCoupon().getCategoryId());
        }
        int discountPercentage = dto.getCoupon().getDiscountPercent();
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < requestedProducts.length; i++) {
            Product product = findProductById(requestedProducts[i]);
            this.checkCartForProduct(user,product);
            if(!isCouponEmpty(dto)){
                addProductToOrderList(product,discountedProduct,discountedCategory,discountPercentage);
            }
            int productId = product.getId();
            cartService.removeProductFromCart(productId,user.getId());
            products.add(product);
        }
        order.setProductsInOrder(products);
        orderRepository.save(order);
        return "Order created successfully";
    }

    private void setDiscountedPrice(Product product,int discountPercentage){
        double regularPrice = product.getRegularPrice();
        double discountedPrice = regularPrice - regularPrice*discountPercentage/100;
        product.setDiscountedPrice(discountedPrice);
    }

    private void checkCartForProduct(User user,Product product){
        boolean isFound = false;
        List<UserCart> productsInCart = user.getProductsInCart();
        for (UserCart userCart : productsInCart) {
            if(userCart.getProduct().getFullName().equals(product.getFullName())){
                isFound = true;
            }
        }
        if(isFound == false) {
            throw new NotFoundException("Product not found in user's cart");
        }
    }

    private void validateVoucher(CreateOrderDTO dto){
        if(!isCouponEmpty(dto)){
            if (dto.getProductsId().length == 0) {
                throw new BadRequestException("You have to select products to make an order");
            }
            int discountPercent = this.getDiscountPercentage(dto);
            if (discountPercent < MIN_PERCENTAGE_VALUE || discountPercent > MAX_PERCENTAGE_VALUE) {
                throw new BadRequestException("Invalid coupon");
            }
            if (dto.getCoupon().getStartDate() != null) {
                LocalDate startDate = dto.getCoupon().getStartDate().toLocalDateTime().toLocalDate();
                if (LocalDate.now().isBefore(startDate)) {
                    throw new BadRequestException("Invalid coupon");
                }
            }else{
                throw new BadRequestException("Invalid coupon");
            }
            if (dto.getCoupon().getEndDate() != null) {
                LocalDate endDate = dto.getCoupon().getEndDate().toLocalDateTime().toLocalDate();
                if (LocalDate.now().isAfter(endDate)) {
                    throw new BadRequestException("Invalid coupon");
                }
            }
        }
    }

    private User findUserById(int id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NotFoundException("User not found!");
        } else {
            Optional<User> userFromDb = userRepository.findById(id);
            return userFromDb.get(); // return userRository...
        }
    }

    private Product findProductById(int productId){
            Optional<Product> product = productRepository.findById(productId);
            if (product.isEmpty()) {
                throw new NotFoundException("Product not found");
            }
            return product.get();
    }

    private Category findCategoryById(int categoryId){
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isEmpty()){
            throw new NotFoundException("Category not found");
        }
        return category.get();
    }

    private boolean isCouponEmpty(CreateOrderDTO dto){
        boolean result = false;
        if(dto.getCoupon().getDiscountPercent() == 0 && dto.getCoupon().getProductId() == 0
        && dto.getCoupon().getCategoryId() == 0 && dto.getCoupon().getStartDate() == null
        && dto.getCoupon().getEndDate() == null){
            result = true;
        }
        return result;
    }

    private int getDiscountPercentage(CreateOrderDTO dto){
        if(dto.getCoupon().getDiscountPercent()==0){
            throw new BadRequestException("Invalid coupon");
        }
        return dto.getCoupon().getDiscountPercent();
    }

    private void addProductToOrderList(Product product, Product discountedProduct, Category discountedCategory, int discountPercentage){
        if(discountedProduct != null) {
            if(product.getFullName().equals(discountedProduct.getFullName())) {
                this.setDiscountedPrice(product, discountPercentage);
            }else{
                throw new BadRequestException("The entered coupon is not valid");
            }
        }else{
            if(discountedCategory != null){
                if(product.getCategory().getName().equals(discountedCategory.getName())) {
                    this.setDiscountedPrice(product, discountPercentage);
                } else {
                    throw new BadRequestException("The entered coupon is not valid");
                }
            } else {
                throw new BadRequestException("The entered coupon is not valid");
            }
        }
    }

}
