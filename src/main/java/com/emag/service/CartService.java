package com.emag.service;

import com.emag.exceptions.AuthenticationException;
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

import javax.servlet.http.HttpSession;
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

    public void addProductToCart(int productId, int userId, HttpSession session){
        this.verifyUserId(userId,session);
        UserCartsKey primaryKey = new UserCartsKey();
        primaryKey.setUserId(userId);
        primaryKey.setProductId(productId);
        Optional<Product> productFromDb = productRepository.findById(productId);
        Optional<User> userFromDb = userRepository.findById(userId);
        Product product = productFromDb.get();
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
    }

    public void removeProductFromCart(int productId,int userId,HttpSession session){
        this.verifyUserId(userId,session);
        userCartDAO.removeProductFromCart(productId,userId);
    }

    public void changeQuantityOfProduct(int productId,int userId,int quantity,HttpSession session){
        this.verifyUserId(userId,session);
        UserCartsKey primaryKey = new UserCartsKey();
        primaryKey.setProductId(productId);
        primaryKey.setUserId(userId);
        Optional<Product> productFromDb = productRepository.findById(productId);
        Optional<User> userFromDb = userRepository.findById(userId);
        Product product = productFromDb.get();
        User user = userFromDb.get();
        if(cartContainsProduct(user,product)) {
            UserCart userCart = this.updateQuantityOfProduct(user, product,quantity);
            cartRepository.save(userCart);
        }
    }

    private boolean cartContainsProduct(User user,Product product){
        boolean result = false;
        List<UserCart> productsInCart = user.getProductsInCart();
        for (UserCart userCarts : productsInCart) {
            if (userCarts.getProduct().getFullName().equals(product.getFullName())){
                result = true;
            }
        }
        return result;
    }

    private UserCart increaseQuantityOfProduct(User user,Product product){
        UserCart userCart = null;
        List<UserCart> productsInCart = user.getProductsInCart();
        for (UserCart cartInDb : productsInCart) {
            if (cartInDb.getProduct().getFullName().equals(product.getFullName())){
                cartInDb.setQuantity(cartInDb.getQuantity()+1);
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
            }
        }
        return userCart;
    }

    private void verifyUserId(int userId,HttpSession session){
        int loggedUserId = (int) session.getAttribute("LOGGED_USER_ID");
        if(userId!=loggedUserId){
            throw new AuthenticationException("You can not make changes to another user's cart!");
        }
    }

}
