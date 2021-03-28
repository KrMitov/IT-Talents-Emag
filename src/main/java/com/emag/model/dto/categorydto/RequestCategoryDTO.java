package com.emag.model.dto.categorydto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestCategoryDTO {

    private String name;
    private Integer parentCategoryId;

}
