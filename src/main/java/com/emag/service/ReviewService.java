package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.model.dto.reviewdto.RequestReviewDTO;
import com.emag.model.dto.reviewdto.ReviewDTO;
import com.emag.model.pojo.Product;
import com.emag.model.pojo.Review;
import com.emag.model.pojo.User;
import com.emag.service.validatorservice.ReviewValidator;
import org.springframework.stereotype.Service;

@Service
public class ReviewService extends AbstractService{

    public ReviewDTO addReview(RequestReviewDTO requestReviewDTO, int userId) {
        ReviewValidator.validateReviewInputData(requestReviewDTO);
        Product reviewedProduct = getProductIfExists(requestReviewDTO.getProductId());
        User reviewer = getUserIfExists(userId);
        if (getReview(reviewedProduct, reviewer) != null){
            throw new BadRequestException("You have already reviewed this product");
        }
        return new ReviewDTO(reviewRepository.save(new Review(requestReviewDTO, reviewedProduct, reviewer)));
    }

    public ReviewDTO editReview(RequestReviewDTO requestReviewDTO, int userId){
        ReviewValidator.validateReviewInputData(requestReviewDTO);
        Product reviewedProduct = getProductIfExists(requestReviewDTO.getProductId());
        User reviewer = getUserIfExists(userId);
        Review review = getReview(reviewedProduct, reviewer);
        if (review == null){
            throw new BadRequestException("You have not reviewed this product");
        }
        review.setTitle(requestReviewDTO.getTitle());
        review.setDescription(requestReviewDTO.getDescription());
        review.setRating(requestReviewDTO.getRating());
        return new ReviewDTO(reviewRepository.save(review));
    }
}
