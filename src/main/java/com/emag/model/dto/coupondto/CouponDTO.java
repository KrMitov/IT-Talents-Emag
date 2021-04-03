package com.emag.model.dto.coupondto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CouponDTO {

    private int userId;
    private int discountPercent;
    private int productId;
    private int categoryId;
    private String startDate;
    private String expireDate;

}
