package com.emag.model.dto.categorydto;

import com.emag.model.pojo.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryAndSubcategoriesDTO {

    private int id;
    private String name;
    private List<CategoryAndSubcategoriesDTO> subcategories;

    public CategoryAndSubcategoriesDTO(Category category) {
        if (category == null){
            return;
        }
        this.id = category.getId();
        this.name = category.getName();
        this.subcategories = new ArrayList<>();
        category.getSubCategories().forEach(subcategory -> subcategories.add(new CategoryAndSubcategoriesDTO(subcategory)));
    }

    public List<CategoryAndSubcategoriesDTO> getSubcategories() {
        return this.subcategories.size() == 0 ? null : this.subcategories;
    }
}
