package com.emag.model.pojo;

import com.emag.model.dto.RequestProductDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    public Product(RequestProductDTO requestProductDTO) {
        this.fullName = requestProductDTO.getFullName();
        this.brand = requestProductDTO.getBrand();
        this.model = requestProductDTO.getModel();
        this.regularPrice = requestProductDTO.getRegularPrice();
        this.discountedPrice = requestProductDTO.getDiscountedPrice();
        this.description = requestProductDTO.getDescription();
        this.quantity = requestProductDTO.getQuantity();
        this.warrantyYears = requestProductDTO.getWarrantyYears();
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
