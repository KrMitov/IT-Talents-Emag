package com.emag.model.dto.produtcDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestProductDTO {

    private String fullName;
    private String brand;
    private String model;
    private Double regularPrice;
    private Double discountedPrice;
    private String description;
    private Integer quantity;
    private Integer warrantyYears;
    private Integer categoryId;
}
