package com.emag.service;

import com.emag.exceptions.AuthenticationException;
import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dao.UserCartDAO;
import com.emag.model.pojo.Product;
import com.emag.model.pojo.User;
import com.emag.model.pojo.UserCart;

import com.emag.model.pojo.UserCartsKey;
import com.emag.model.repository.CartRepository;
import com.emag.model.repository.ProductRepository;
import com.emag.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserCartDAO userCartDAO;
    private static final int PRODUCT_MIN_QUANTITY = 1;
    private static final int PRODUCT_MAX_QUANTITY = 50;

    public String addProductToCart(int productId, int userId){
        UserCartsKey primaryKey = new UserCartsKey();
        primaryKey.setUserId(userId);
        primaryKey.setProductId(productId);
        Optional<User> userFromDb = userRepository.findById(userId);
        Product product = findProductById(productId);
        User user = userFromDb.get();
       if(cartContainsProduct(user,product)){
           UserCart userCart = this.increaseQuantityOfProduct(user,product);
           cartRepository.save(userCart);
       }else {
           int quantity = 1;
           UserCart userCart = new UserCart();
           userCart.setPrimaryKey(primaryKey);
           userCart.setUser(user);
           userCart.setProduct(product);
           userCart.setQuantity(quantity);
           cartRepository.save(userCart);
       }
       return "Product added to cart successfully";
    }

    public String removeProductFromCart(int productId,int userId){
        userCartDAO.removeProductFromCart(productId,userId);
        return "Product removed from cart successfully";
    }

    public String changeQuantityOfProduct(int productId,int userId,int quantity){
        if (quantity < PRODUCT_MIN_QUANTITY || quantity > PRODUCT_MAX_QUANTITY){
            throw new BadRequestException("Wrong quantity for product");
        }
        UserCartsKey primaryKey = new UserCartsKey();
        primaryKey.setProductId(productId);
        primaryKey.setUserId(userId);
        Optional<User> userFromDb = userRepository.findById(userId);
        Product product = findProductById(productId);
        User user = userFromDb.get();
        if(cartContainsProduct(user,product)) {
            UserCart userCart = this.updateQuantityOfProduct(user, product,quantity);
            cartRepository.save(userCart);
        }else{
            throw new NotFoundException("The product was not found in user's cart");
        }
        return "Product quantity changed successfully";
    }

    private boolean cartContainsProduct(User user,Product product){
        boolean result = false;
        List<UserCart> productsInCart = user.getProductsInCart();
        for (UserCart userCarts : productsInCart) {
            if (userCarts.getProduct().getFullName().equals(product.getFullName())){
                result = true;
                break;
            }
        }
        return result;
    }

    private UserCart increaseQuantityOfProduct(User user, Product product) {
        UserCart userCart = null;
        List<UserCart> productsInCart = user.getProductsInCart();
        for (UserCart cartInDb : productsInCart) {
            if (cartInDb.getProduct().getFullName().equals(product.getFullName())) {
                cartInDb.setQuantity(cartInDb.getQuantity() + 1);
                userCart = cartInDb;
            }
        }
        return userCart;
    }

    private UserCart updateQuantityOfProduct(User user, Product product, int quantity){
        UserCart userCart = null;
        List<UserCart> productsInCart = user.getProductsInCart();
        for (UserCart cartInDb : productsInCart) {
            if(cartInDb.getProduct().getFullName().equals(product.getFullName())){
                cartInDb.setQuantity(quantity);
                userCart = cartInDb;
                break;
            }
        }
        return userCart;
    }

    private Product findProductById(int productId){
        Optional<Product> product = productRepository.findById(productId);
        if(product.isEmpty()){
            throw new NotFoundException("Product not found");
        }
        return product.get();
    }

}
