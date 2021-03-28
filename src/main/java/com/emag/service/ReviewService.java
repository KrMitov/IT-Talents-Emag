package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.model.dto.reviewdto.RequestReviewDTO;
import com.emag.model.dto.reviewdto.ReviewDTO;
import com.emag.model.pojo.Product;
import com.emag.model.pojo.Review;
import com.emag.model.pojo.User;
import com.emag.model.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductService productService;

    //TODO move to util class
    private void validateReviewInputData(RequestReviewDTO requestReviewDTO){
        if (requestReviewDTO.getProductId() == null || requestReviewDTO.getProductId() <= 0){
            throw new BadRequestException("Invalid product id");
        }
        if (requestReviewDTO.getTitle() == null || requestReviewDTO.getTitle().trim().equals("")){
            throw new BadRequestException("Invalid product title");
        }
        if (requestReviewDTO.getDescription() == null || requestReviewDTO.getDescription().trim().equals("")){
            throw new BadRequestException("Invalid product description");
        }
        if (requestReviewDTO.getRating() == null || requestReviewDTO.getRating() < 0 || requestReviewDTO.getRating() > 5){
            throw new BadRequestException("Invalid product rating! Product rating should be between 0 and 5");
        }
    }

    //TODO move to util class
    private Review getReview(Product product, User user){
        return reviewRepository.findByReviewedProductAndReviewer(product, user);
    }

    public ReviewDTO addReview(RequestReviewDTO requestReviewDTO, int userId) {
        validateReviewInputData(requestReviewDTO);
        Product reviewedProduct = productService.getProductIfExists(requestReviewDTO.getProductId());
        //TODO remove getUserIfExists method from productService class
        User reviewer = productService.getUserIfExists(userId);
        if (getReview(reviewedProduct, reviewer) != null){
            throw new BadRequestException("You have already reviewed this product");
        }
        return new ReviewDTO(reviewRepository.save(new Review(requestReviewDTO, reviewedProduct, reviewer)));
    }

    public ReviewDTO editReview(RequestReviewDTO requestReviewDTO, int userId){
        validateReviewInputData(requestReviewDTO);
        Product reviewedProduct = productService.getProductIfExists(requestReviewDTO.getProductId());
        //TODO remove getUserIfExists method from productService class
        User reviewer = productService.getUserIfExists(userId);
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
