package com.emag.service.validatorservice;

import com.emag.exceptions.BadRequestException;
import com.emag.model.dto.produtcdto.RequestProductDTO;

public class ProductValidator {

    public static void validateProductInputData(RequestProductDTO requestProductDTO){
        if (requestProductDTO.getFullName() == null || requestProductDTO.getFullName().trim().equals("")){
            throw new BadRequestException("Invalid product name");
        }
        if (requestProductDTO.getBrand() == null || requestProductDTO.getBrand().trim().equals("")){
            throw new BadRequestException("Invalid brand name");
        }
        if (requestProductDTO.getModel() == null || requestProductDTO.getModel().trim().equals("")){
            throw new BadRequestException("Invalid model name");
        }
        if (requestProductDTO.getRegularPrice() == null || requestProductDTO.getRegularPrice() <= 0){
            throw new BadRequestException("Invalid regular price for product");
        }
        if (requestProductDTO.getDiscountedPrice() != null && (requestProductDTO.getDiscountedPrice() <= 0
                || requestProductDTO.getDiscountedPrice() >= requestProductDTO.getRegularPrice())){
            throw new BadRequestException("Invalid discounted price for product");
        }
        if (requestProductDTO.getDescription() != null && requestProductDTO.getDescription().trim().equals("")){
            requestProductDTO.setDescription(null);
        }
        if (requestProductDTO.getQuantity() == null || requestProductDTO.getQuantity() <= 0){
            throw new BadRequestException("Invalid product quantity");
        }
        if (requestProductDTO.getWarrantyYears() != null && requestProductDTO.getWarrantyYears() < 0){
            throw new BadRequestException("Invalid warranty years for product");
        }
    }
}
