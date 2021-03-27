package com.emag.model.dto.categoryDTO;

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

    private int id;
    private String name;
    private CategoryDTO parentCategory;

    public CategoryDTO(Category category) {
        if (category == null){
            return;
        }
        this.id = category.getId();
        this.name = category.getName();
        Category pCategory = category.getParentCategory();
        this.parentCategory = pCategory == null ? null : new CategoryDTO(pCategory);
    }
}
