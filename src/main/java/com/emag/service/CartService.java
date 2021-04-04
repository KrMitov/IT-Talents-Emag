package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dao.UserCartDAO;
import com.emag.model.dto.produtcdto.ProductDTO;
import com.emag.model.dto.userdto.UserCartDTO;
import com.emag.model.pojo.Product;
import com.emag.model.pojo.User;
import com.emag.model.pojo.UserCart;

import com.emag.model.pojo.UserCartsKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;

@Service
public class CartService extends AbstractService{

    @Autowired
    UserCartDAO userCartDAO;
    private static final int PRODUCT_MIN_QUANTITY = 1;
    private static final int PRODUCT_MAX_QUANTITY = 50;

    @Transactional
    public UserCartDTO addProductToCart(int productId, int userId){
        UserCartsKey primaryKey = new UserCartsKey();
        primaryKey.setUserId(userId);
        primaryKey.setProductId(productId);
        Product product = getProductIfExists(productId);
        User user = getUserIfExists(userId);
        if(cartContainsProduct(user,product)){
           UserCart userCart = this.increaseQuantityOfProduct(user,product);
           cartRepository.save(userCart);
           return new UserCartDTO(userCart);
       }else {
           int quantity = 1;
           UserCart userCart = new UserCart();
           userCart.setPrimaryKey(primaryKey);
           userCart.setUser(user);
           userCart.setProduct(product);
           userCart.setQuantity(quantity);
           cartRepository.save(userCart);
           return new UserCartDTO(userCart);
        }
    }

    public UserCartDTO removeProductFromCart(int productId,int userId) throws SQLException {
        User user = getUserIfExists(userId);
        Product product = getProductIfExists(productId);
        if (cartContainsProduct(user, product)){
            userCartDAO.removeProductFromCart(productId, userId);
            int quantity = this.getQuantityOfProduct(user,product);
            UserCartDTO dto = new UserCartDTO();
            dto.setProduct(new ProductDTO(product));
            dto.setQuantity(quantity);
            return dto;
        } else {
            throw new NotFoundException("The product was not found in user's cart");
        }
    }

    public UserCartDTO changeQuantityOfProduct(int productId,int userId,int quantity){
        if (quantity < PRODUCT_MIN_QUANTITY || quantity > PRODUCT_MAX_QUANTITY){
            throw new BadRequestException("Wrong quantity for product");
        }
        UserCartsKey primaryKey = new UserCartsKey();
        primaryKey.setProductId(productId);
        primaryKey.setUserId(userId);
        Product product = getProductIfExists(productId);
        User user = getUserIfExists(userId);
        if(cartContainsProduct(user,product)) {
            UserCart userCart = this.updateQuantityOfProduct(user, product,quantity);
            cartRepository.save(userCart);
            return new UserCartDTO(userCart);
        }else{
            throw new NotFoundException("The product was not found in user's cart");
        }
    }

    private int getQuantityOfProduct(User user,Product product){
        int quantity = 0;
        for (UserCart userCart : user.getProductsInCart()) {
            if(userCart.getProduct().getFullName().equals(product.getFullName())){
                quantity = userCart.getQuantity();
                break;
            }
        }
        return quantity;
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

}
