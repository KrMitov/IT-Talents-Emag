package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dto.categorydto.RequestCategoryDTO;
import com.emag.model.dto.categorydto.CategoryAndSubcategoriesDTO;
import com.emag.model.dto.categorydto.CategoryDTO;
import com.emag.model.dto.produtcdto.ProductDTO;
import com.emag.model.pojo.Category;
import com.emag.model.pojo.Product;
import com.emag.service.validatorservice.CategoryValidator;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CategoryService extends AbstractService{

    public List<ProductDTO> getProductsFromCategory(int id){
        Category category;
        try {
            category = getCategoryIfExists(id);
        }
        catch (BadRequestException e){
            throw new NotFoundException("Category not found");
        }
        List<Product> productsForCategory = category.getProducts();
        List<ProductDTO> productsDTOList = new ArrayList<>();
        productsForCategory.forEach(product -> productsDTOList.add(new ProductDTO(product)));
        for (Category subCategory : category.getSubCategories()){
            productsDTOList.addAll(getProductsFromCategory(subCategory.getId()));
        }
        return productsDTOList;
    }

    public CategoryDTO addCategory(RequestCategoryDTO requestCategoryDTO) {
        String trimmedName = requestCategoryDTO.getName().trim();
        CategoryValidator.validateCategoryName(trimmedName);
        Category category = new Category();
        category.setName(trimmedName);
        category.setParentCategory(getParentCategory(requestCategoryDTO.getParentCategoryId()));
        return new CategoryDTO(categoryRepository.save(category));
    }

    public CategoryDTO editCategory(int id, RequestCategoryDTO requestCategoryDTO) {
        Category editedCategory = getCategoryIfExists(id);
        String trimmedName = requestCategoryDTO.getName().trim();
        CategoryValidator.validateCategoryName(trimmedName);
        editedCategory.setName(trimmedName);
        editedCategory.setParentCategory(getParentCategory(requestCategoryDTO.getParentCategoryId()));
        return new CategoryDTO(categoryRepository.save(editedCategory));
    }

    public CategoryAndSubcategoriesDTO deleteCategory(int id){
        Category category = getCategoryIfExists(id);
        CategoryAndSubcategoriesDTO deletedCategory = new CategoryAndSubcategoriesDTO(category);
        categoryRepository.deleteById(id);
        return deletedCategory;
    }

    public List<CategoryAndSubcategoriesDTO> getAllCategories() {
        List<CategoryAndSubcategoriesDTO> categories = new ArrayList<>();
        List<Category> categoriesWithoutParent = categoryRepository.findAllByParentCategoryIsNull();
        if (categoriesWithoutParent.isEmpty()){
            throw new NotFoundException("No categories found");
        }
        categoriesWithoutParent.forEach(category -> categories.add(new CategoryAndSubcategoriesDTO(category)));
        return categories;
    }
}
