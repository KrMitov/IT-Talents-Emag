package com.emag.model.dto.orderdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class CouponDTO {

    private int discountPercent;
    private int productId;
    private int categoryId;
    private Timestamp startDate;
    private Timestamp endDate;

}
