package com.emag.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterProductsDTO {

    private Integer categoryId;
    private String searchKeyword;
    private String brand;
    private String model;
    private Double maxPrice;
    private Double minPrice;
    private Boolean discountedOnly;
    private Boolean orderByPrice;
    private Boolean sortDesc;
    private Integer productsPerPage;
    private Integer pageNumber;
}
