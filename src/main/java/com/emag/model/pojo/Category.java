package com.emag.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToOne()
    @JoinColumn(name="parent_category_id")
    private Category parentCategory;
    @OneToMany(mappedBy = "parentCategory")
    private List<Category> subCategories;
    @OneToMany(mappedBy = "category")
    private List<Product> products;

    public List<Product> getProducts() {
        List<Product> availableProducts = new ArrayList<>();
        this.products.forEach(product -> {
            if (product.getDeletedAt() == null){
                availableProducts.add(product);
            }
        });
        return availableProducts;
    }
}
