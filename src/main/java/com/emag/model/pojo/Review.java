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
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private int rating;
    private LocalDateTime createdAt;
    private int likes;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product reviewedProduct;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reviewer;
}
