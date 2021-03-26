package com.emag.model.dto;

import com.emag.model.pojo.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {

    private String fullName;
    private String brand;
    private String model;
    private double regularPrice;
    private Double discountedPrice;
    private String description;
    private Integer quantity;
    private int warrantyYears;
    private CategoryDTO category;
    //private List<Review> reviews;

    public ProductDTO(Product product) {
        this.fullName = product.getFullName();
        this.brand = product.getBrand();
        this.model = product.getModel();
        this.regularPrice = product.getRegularPrice();
        this.discountedPrice = product.getDiscountedPrice();
        this.description = product.getDescription();
        this.quantity = product.getQuantity();
        this.warrantyYears = product.getWarrantyYears();
        this.category = new CategoryDTO(product.getCategory());
        //this.reviews = new ArrayList<>();
    }
}
