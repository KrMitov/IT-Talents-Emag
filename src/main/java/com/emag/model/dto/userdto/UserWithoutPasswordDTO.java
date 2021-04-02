package com.emag.model.dto.userdto;

import com.emag.model.dto.produtcdto.ProductDTO;
import com.emag.model.dto.roledto.RoleWithoutUsersDTO;
import com.emag.model.pojo.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
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
    private List<ProductDTO> likedProducts;
    private List<ProductDTO> productsInCart;
    private RoleWithoutUsersDTO role;
    private Date birthdate;
    private Timestamp createdAt;
    private UserImage profilePicture;
    private String phoneNumber;

    public UserWithoutPasswordDTO(User user){
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.name = user.getName();
        this.likedProducts = new ArrayList<>();
        for (Product likedProduct : user.getLikedProducts()) {
            this.likedProducts.add(new ProductDTO(likedProduct));
        }
        this.productsInCart = new ArrayList<>();
        for (UserCart userCart : user.getProductsInCart()) {
            this.productsInCart.add(new ProductDTO(userCart.getProduct()));
        }
        this.phoneNumber = user.getPhoneNumber();
        this.role = new RoleWithoutUsersDTO(user.getRole());
        if(user.getBirthDate()!=null) {
            this.birthdate = user.getBirthDate();
        }
        this.createdAt = user.getCreatedAt();
        this.profilePicture = user.getImage();
    }

}
