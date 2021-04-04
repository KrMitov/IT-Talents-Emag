package com.emag.model.dto.coupondto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CouponDTO {

    @NotNull(message = "Discount percent must be between 1 and 95")
    @Min(value=1, message="Discount percent must be between 1 and 95")
    private Integer discountPercent;
    @NotNull(message = "Product id must not be null")
    private Integer productId;
    @NotNull(message = "Category id must not be null")
    private Integer categoryId;
    private String startDate;
    private String expireDate;

}
