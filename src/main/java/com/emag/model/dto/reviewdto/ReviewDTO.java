package com.emag.model.dto.reviewdto;

import com.emag.model.pojo.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewDTO {

    private String title;
    private String description;
    private int rating;
    private LocalDateTime createdAt;
    private int likes;
    private String reviewerName;

    public ReviewDTO(Review review) {
        this.title = review.getTitle();
        this.description = review.getDescription();
        this.rating = review.getRating();
        this.likes = review.getLikes();
        this.createdAt = review.getCreatedAt();
        this.reviewerName = review.getReviewer().getName();
    }
}
