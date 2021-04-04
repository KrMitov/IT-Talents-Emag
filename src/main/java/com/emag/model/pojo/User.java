package com.emag.model.pojo;

import com.emag.model.dto.registerdto.RegisterRequestUserDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Date;
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
        @ManyToMany
        @JsonManagedReference
        @JoinTable(
                name="saved_addresses",
                joinColumns = {@JoinColumn(name="user_id")},
                inverseJoinColumns = {@JoinColumn(name="address_id")}
        )
        private List<Address> addresses;
        @ManyToOne
        @JsonBackReference
        @JoinColumn(name="role_id")
        private Role role;
        private Date birthDate;
        private Timestamp createdAt;
        private String phoneNumber;
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
        private List<UserCart> productsInCart;
        @OneToOne
        @JoinColumn(name = "image_id")
        @JsonManagedReference
        private UserImage image;
        @ManyToMany
        @JsonManagedReference
        @JoinTable(
                name="users_like_reviews",
                joinColumns = {@JoinColumn(name="user_id")},
                inverseJoinColumns = {@JoinColumn(name="review_id")}
        )
        private List<Review> likedReviews;
       @OneToMany(mappedBy = "userHasOrder")
       @JsonManagedReference
       private List<Order> orders;

           public User(RegisterRequestUserDTO registerRequestUserDTO){
            this.email = registerRequestUserDTO.getEmail().trim();
            this.password = registerRequestUserDTO.getPassword().trim();
            this.name = registerRequestUserDTO.getName().trim();
            this.createdAt = Timestamp.valueOf(LocalDateTime.now());
        }
}
