package com.emag.util;

import com.emag.model.dto.produtcdto.RequestProductDTO;

public class ProductUtility {

    public static RequestProductDTO trimProductInputData(RequestProductDTO requestProductDTO){
        requestProductDTO.setFullName(requestProductDTO.getFullName().trim());
        requestProductDTO.setBrand(requestProductDTO.getBrand().trim());
        requestProductDTO.setModel(requestProductDTO.getModel().trim());
        if (requestProductDTO.getDescription() != null) {
            requestProductDTO.setDescription(requestProductDTO.getDescription().trim());
        }
        return requestProductDTO;
    }
}
