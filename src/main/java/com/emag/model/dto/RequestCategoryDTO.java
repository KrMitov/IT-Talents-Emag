package com.emag.model.dto;

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

    public int getParentCategoryId() {
        //when parentCategoryId from the response body is 0 or null
        //that means the category has no parent category
        //and this getter returns 0 in both cases
        return this.parentCategoryId == null ? 0 : this.parentCategoryId;
    }
}
