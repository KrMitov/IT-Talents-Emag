package com.emag.model.dto.reviewdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestReviewDTO {

    private Integer productId;
    private String title;
    private String description;
    private Integer rating;
}
