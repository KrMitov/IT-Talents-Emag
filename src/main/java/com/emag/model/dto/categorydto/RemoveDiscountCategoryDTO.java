package com.emag.model.dto.categorydto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RemoveDiscountCategoryDTO {

    private List<Integer> categoryIds;
}
