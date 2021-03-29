package com.emag.service;

import com.emag.exceptions.NotFoundException;
import com.emag.model.dto.orderdto.CreateOrderDTO;
import com.emag.model.pojo.Category;
import com.emag.model.pojo.Order;
import com.emag.model.pojo.Product;
import com.emag.model.pojo.User;
import com.emag.model.repository.CategoryRepository;
import com.emag.model.repository.OrderRepository;
import com.emag.model.repository.ProductRepository;
import com.emag.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
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

    @Transactional
    public void createOrder(CreateOrderDTO dto){
        int userId = dto.getUserId();
        Optional<Product> discountedProductFromDb = productRepository.findById(dto.getCoupon().getProductId());
        Product discountedProduct = null;
        if(discountedProductFromDb.isPresent()){
            discountedProduct = discountedProductFromDb.get();
        }
        Optional<Category> discountedCategoryFromDb = categoryRepository.findById(dto.getCoupon().getCategoryId());
        Category discountedCategory = null;
        if(discountedCategoryFromDb.isPresent()) {
            discountedCategory = discountedCategoryFromDb.get();
        }
        int discountPercentage = dto.getCoupon().getDiscountPercent();
        Optional<User> userFromDb = userRepository.findById(userId);
        if(userFromDb.isEmpty()){
            throw new NotFoundException("User not found");
        }
        User user = userFromDb.get();
        Order order = new Order();
        order.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        order.setUserHasOrder(user);
        int[] requestedProducts = dto.getProductsId();
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < requestedProducts.length; i++) {
            Optional<Product> productFromDb = productRepository.findById(requestedProducts[i]);
            if(productFromDb.isEmpty()){
                throw new NotFoundException("Product not found");
            }
            Product product = productFromDb.get();
            if(discountedProduct!=null){
                if(product.getFullName().equals(discountedProduct.getFullName())){
                    this.setDiscountedPrice(product,discountPercentage);
                }
            }else{
                if (discountedCategory != null) {
                    if (product.getCategory().getName().equals(discountedCategory.getName())) {
                        this.setDiscountedPrice(product, discountPercentage);
                    }
                }
            }
            int productId = product.getId();
            cartService.removeProductFromCart(productId,userId);
            products.add(product);
        }
        order.setProductsInOrder(products);
        orderRepository.save(order);
    }

    private void setDiscountedPrice(Product product,int discountPercentage){
        double regularPrice = product.getRegularPrice();
        double discountedPrice = regularPrice - regularPrice*discountPercentage/100;
        product.setDiscountedPrice(discountedPrice);
    }

}
