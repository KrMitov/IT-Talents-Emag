package com.emag.model.dto.cartdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CartDTO {

    @NotNull(message = "You have to enter product id")
    @Min(value=1, message="Enter a correct product id")
    private int productId;
    @NotNull(message = "You have to enter user id")
    @Min(value=1, message="enter a correct user id")
    private int userId;
}
