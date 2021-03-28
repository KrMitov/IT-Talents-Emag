package com.emag.model.pojo;

import com.emag.model.dto.registerdto.RegisterRequestUserDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        private String nickname;
        private String email;
        private String password;
        private String name;
        //    @OneToMany(mappedBy = "user")
//    @JsonManagedReference
        @ManyToMany
        @JsonManagedReference
        @JoinTable(
                name="saved_addresses",
                joinColumns = {@JoinColumn(name="user_id")},
                inverseJoinColumns = {@JoinColumn(name="address_id")}
        )
        private List<Address> addresses;
        private String phoneNumber;
        @ManyToOne
        @JsonBackReference
        @JoinColumn(name="role_id")
        private Role role;
        private Timestamp birthDate;
        private Timestamp createdAt;
        private String profile_picture;
        @ManyToMany
        @JsonManagedReference
        @JoinTable(
            name="users_like_products",
                    joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="product_id")}

        )
        private List<Product> likedProducts;
        @OneToMany(mappedBy = "reviewer")
        private List<Review> reviews;
        @OneToMany(mappedBy = "user")
        private List<UserCarts> productsInCart;

        public User(RegisterRequestUserDTO registerRequestUserDTO){
            this.email = registerRequestUserDTO.getEmail();
            this.password = registerRequestUserDTO.getPassword();
            this.name = registerRequestUserDTO.getName();
            this.createdAt = Timestamp.valueOf(LocalDateTime.now());
        }
}
