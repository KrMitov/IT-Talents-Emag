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

    private String full_name;
    private String brand;
    private String model;
    private double regular_price;
    private Double discounted_price;
    private String description;
    private Integer quantity;
    private int warranty_years;
    private CategoryDTO category;
    //private List<Review> reviews;

    public ProductDTO(Product product) {
        this.full_name = product.getFull_name();
        this.brand = product.getBrand();
        this.model = product.getModel();
        this.regular_price = product.getRegular_price();
        this.discounted_price = product.getDiscounted_price();
        this.description = product.getDescription();
        this.quantity = product.getQuantity();
        this.warranty_years = product.getWarranty_years();
        this.category = new CategoryDTO(product.getCategory());
        //this.reviews = new ArrayList<>();
    }
}
