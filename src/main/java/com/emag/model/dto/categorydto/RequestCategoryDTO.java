package com.emag.model.dto.categorydto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestCategoryDTO {

    @NotBlank(message = "Category name is mandatory")
    private String name;
    @Min(value = 0, message = "Parent category id should not be less than 0")
    private Integer parentCategoryId;

}
