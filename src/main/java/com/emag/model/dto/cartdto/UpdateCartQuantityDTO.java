package com.emag.model.dto.cartdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCartQuantityDTO {
    @NotNull
    @Min(value=1, message="You have to enter a correct user id")
    private int userId;
    @NotNull
    @Min(value=1, message="You have to enter a correct product id")
    private int productId;
    @NotNull
    @Min(value=1, message="You have to enter correct quantity")
    @Max(value=50, message="You have to enter correct quantity - max cart quantity is 50")
    private int quantity;
}
