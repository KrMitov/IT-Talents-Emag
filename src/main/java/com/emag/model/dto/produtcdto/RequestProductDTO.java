package com.emag.model.dto.produtcdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestProductDTO {

    @NotBlank(message = "Full name is mandatory")
    private String fullName;
    @NotBlank(message = "Brand is mandatory")
    private String brand;
    @NotBlank(message = "Model is mandatory")
    private String model;
    @NotNull(message = "Regular price is mandatory")
    @Min(value = 1, message = "Regular price should not be less than 1")
    private Double regularPrice;
    @Min(value = 1, message = "Discounted price should not be less than 1")
    private Double discountedPrice;
    private String description;
    @NotNull(message = "Quantity is mandatory")
    @Min(value = 1, message = "Quantity should not be less than 1")
    private Integer quantity;
    @Min(value = 1, message = "Warranty years should not be less than 1")
    private Integer warrantyYears;
    @NotNull(message = "Category id is mandatory")
    @Min(value = 1, message = "Category id should not be less than 1")
    private Integer categoryId;
}
