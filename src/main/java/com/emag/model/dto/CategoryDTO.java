package com.emag.model.dto;

import com.emag.model.pojo.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO {

    private String name;
    private CategoryDTO parentCategory;

    public CategoryDTO(Category category) {
        if (category == null){
            return;
        }
        this.name = category.getName();
        if (category.getParentCategory() == null){
            this.parentCategory = null;
        }
        else {
            this.parentCategory = new CategoryDTO(category.getParentCategory());
        }
    }
}
