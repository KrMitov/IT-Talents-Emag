package com.emag.model.dto.orderdto;

import com.emag.model.dto.coupondto.CouponDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CreateOrderDTO {
    @NotNull(message = "You need to select products to make an order")
    private int[] productsId;
    @NotNull
    @Min(value = 1,message = "You need to add user id")
    private int userId;
    @Valid
    private CouponDTO coupon;
}
