package com.emag.model.pojo;

import com.emag.model.dto.reviewdto.RequestReviewDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    @ManyToMany(mappedBy = "likedReviews")
    @JsonBackReference
    private List<User> usersLikedThisReview;

    public Review(RequestReviewDTO requestReviewDTO, Product product, User user) {
        this.title = requestReviewDTO.getTitle();
        this.description = requestReviewDTO.getDescription();
        this.rating = requestReviewDTO.getRating();
        this.createdAt = LocalDateTime.now();
        this.likes = 0;
        this.reviewedProduct = product;
        this.reviewer = user;
    }
}
