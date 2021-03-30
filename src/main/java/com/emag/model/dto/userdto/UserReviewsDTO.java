package com.emag.model.dto.userdto;

import com.emag.model.dto.reviewdto.ReviewDTO;
import com.emag.model.pojo.Review;
import com.emag.model.pojo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserReviewsDTO {
    private List<ReviewDTO> reviews;

    public UserReviewsDTO(User user){
        this.reviews = new ArrayList<>();
        for (Review review : user.getReviews()) {
            ReviewDTO reviewDTO = new ReviewDTO(review);
            reviews.add(reviewDTO);
        }
    }
}
