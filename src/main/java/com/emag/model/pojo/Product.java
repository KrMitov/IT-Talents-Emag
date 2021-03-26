package com.emag.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private int warrantyYears;
    private LocalDateTime deletedAt;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
