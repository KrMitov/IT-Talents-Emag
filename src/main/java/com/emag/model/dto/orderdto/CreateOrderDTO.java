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
    @NotNull(message = "You need to add user id")
    @Min(value = 1,message = "You need to add a valid user id")
    private int userId;
    @NotNull(message = "Coupon id must be between 0 and 100")
    @Min(value = 0,message = "Enter a valid coupon")
    private int couponId;
}
