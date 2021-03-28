package com.emag.model.dto.userdto;

import com.emag.model.dto.produtcdto.ProductDTO;
import com.emag.model.dto.roledto.RoleWithoutUsersDTO;
import com.emag.model.pojo.Address;
import com.emag.model.pojo.Product;
import com.emag.model.pojo.User;
import com.emag.model.pojo.UserCarts;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserWithoutPasswordDTO {

    private int id;
    private String nickname;
    private String email;
    private String name;
    private List<Address> addresses;
    private String phoneNumber;
    private RoleWithoutUsersDTO role;
    private Timestamp birthdate;
    private Timestamp createdAt;
    private String profile_picture;
    private List<ProductDTO> likedProducts;
    private List<UserCartDTO> productsInCart;

    public UserWithoutPasswordDTO(User user){
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.name = user.getName();
        this.addresses = user.getAddresses();
        this.phoneNumber = user.getPhoneNumber();
        this.role = new RoleWithoutUsersDTO(user.getRole());
        if(user.getBirthDate()!=null) {
            this.birthdate = user.getBirthDate();
        }
        this.createdAt = user.getCreatedAt();
        this.profile_picture = user.getProfile_picture();
        this.likedProducts = new ArrayList<>();
        for (Product product : user.getLikedProducts()) {
            ProductDTO dto = new ProductDTO(product);
            this.likedProducts.add(dto);
        }
        this.productsInCart = new ArrayList<>();
        for (UserCarts userCart : user.getProductsInCart()) {
            UserCartDTO dto = new UserCartDTO(userCart);
            this.productsInCart.add(dto);
        }
    }

}
