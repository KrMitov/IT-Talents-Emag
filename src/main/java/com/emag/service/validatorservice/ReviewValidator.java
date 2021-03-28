package com.emag.service.validatorservice;

import com.emag.exceptions.BadRequestException;
import com.emag.model.dto.reviewdto.RequestReviewDTO;

public class ReviewValidator {

    public static void validateReviewInputData(RequestReviewDTO requestReviewDTO){
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
}
