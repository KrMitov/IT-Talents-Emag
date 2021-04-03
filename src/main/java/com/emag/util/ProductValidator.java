package com.emag.util;

import com.emag.exceptions.BadRequestException;
import com.emag.model.dto.produtcdto.RequestProductDTO;

public class ProductValidator {

    public static void validateProductInputData(RequestProductDTO requestProductDTO){
        if (requestProductDTO.getDiscountedPrice() != null && requestProductDTO.getDiscountedPrice() >= requestProductDTO.getRegularPrice()){
            throw new BadRequestException("Discounted price should be lower than the regular price");
        }
        if (requestProductDTO.getDescription() != null && requestProductDTO.getDescription().trim().equals("")){
            requestProductDTO.setDescription(null);
        }
    }
}
