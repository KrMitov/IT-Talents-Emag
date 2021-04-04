package com.emag.model.dto.produtcdto;

import com.emag.model.dto.categorydto.CategoryDTO;
import com.emag.model.dto.reviewdto.ReviewDTO;
import com.emag.model.pojo.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductFromOrderDTO {
    private int id;
    private String fullName;
    private String brand;
    private String model;
    private double regularPrice;
    private Double discountedPrice;
    private String description;
    private Integer quantity;
    private Integer warrantyYears;
    private CategoryDTO category;
    private List<ReviewDTO> reviews;
    private List<Integer> imagesIds;
    private int orderedQuantity;

    public ProductFromOrderDTO(Product product,int quantity) {
        this.id = product.getId();
        this.fullName = product.getFullName();
        this.brand = product.getBrand();
        this.model = product.getModel();
        this.regularPrice = product.getRegularPrice();
        this.discountedPrice = product.getDiscountedPrice();
        this.description = product.getDescription();
        this.quantity = product.getQuantity();
        this.warrantyYears = product.getWarrantyYears();
        this.category = new CategoryDTO(product.getCategory());
        this.reviews = new ArrayList<>();
        product.getReviews().forEach(review -> reviews.add(new ReviewDTO(review)));
        this.imagesIds = new ArrayList<>();
        if (product.getProductImages() != null) {
            product.getProductImages().forEach(image -> imagesIds.add(image.getId()));
        }
        this.orderedQuantity = quantity;
    }
}
