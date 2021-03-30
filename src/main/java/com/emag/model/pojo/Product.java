package com.emag.model.pojo;

import com.emag.model.dto.produtcdto.RequestProductDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fullName;
    private String brand;
    private String model;
    private double regularPrice;
    private Double discountedPrice;
    private String description;
    private Integer quantity;
    private Integer warrantyYears;
    private LocalDateTime deletedAt;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToMany(mappedBy = "likedProducts")
    @JsonBackReference
    private List<User> usersLikedThisProduct;
    @OneToMany(mappedBy = "reviewedProduct")
    private List<Review> reviews;
    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<UserCart> productsInCart;
    @ManyToMany(mappedBy = "productsInOrder")
    @JsonBackReference
    List<Order> productIsInOrder;
    @OneToMany(mappedBy = "productHasCoupon")
    @JsonBackReference
    private List<Coupon> coupons;
    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<ProductImage> productImages;

    public Product(RequestProductDTO requestProductDTO) {
        this.fullName = requestProductDTO.getFullName();
        this.brand = requestProductDTO.getBrand();
        this.model = requestProductDTO.getModel();
        this.regularPrice = requestProductDTO.getRegularPrice();
        this.discountedPrice = requestProductDTO.getDiscountedPrice();
        this.description = requestProductDTO.getDescription();
        this.quantity = requestProductDTO.getQuantity();
        this.warrantyYears = requestProductDTO.getWarrantyYears();
        this.reviews = new ArrayList<>();
        this.usersLikedThisProduct = new ArrayList<>();
        this.productsInCart = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
