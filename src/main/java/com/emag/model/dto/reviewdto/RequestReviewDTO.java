package com.emag.model.dto.reviewdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestReviewDTO {

    @NotNull(message = "Product id is mandatory")
    @Min(value = 1, message = "Product id should not be less than 1")
    private Integer productId;
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Description is mandatory")
    private String description;
    @Min(value = 1, message = "Review rating should not be less than 1")
    @Max(value = 5, message = "Review rating should not be greater than 5")
    private Integer rating;
}
