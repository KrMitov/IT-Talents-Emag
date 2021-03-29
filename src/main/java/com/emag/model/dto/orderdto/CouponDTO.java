package com.emag.model.dto.orderdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CouponDTO {

    private int discountPercent;
    private int productId;
    private int categoryId;
    private String startDate;
    private String endDate;

}
