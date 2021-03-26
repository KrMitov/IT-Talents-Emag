package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dto.RequestCategoryDTO;
import com.emag.model.dto.CategoryAndSubcategoriesDTO;
import com.emag.model.dto.CategoryDTO;
import com.emag.model.dto.ProductDTO;
import com.emag.model.pojo.Category;
import com.emag.model.pojo.Product;
import com.emag.model.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ProductDTO> getProductsFroCategory(int id) throws NotFoundException {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null){
            throw new NotFoundException("Category not found");
        }
        List<Product> productsForCategory = category.getProducts();
        if (productsForCategory.size() == 0){
            throw new BadRequestException("No products for category");
        }
        List<ProductDTO> productsDTOList = new ArrayList<>();
        productsForCategory.forEach(product -> productsDTOList.add(new ProductDTO(product)));
        return productsDTOList;
    }

    private void validateCategoryInputData(RequestCategoryDTO requestCategoryDTO){
        String name = requestCategoryDTO.getName();
        if (name == null || name.equals("")){
            throw new BadRequestException("Category name cannot be null or empty string");
        }
        if (requestCategoryDTO.getParentCategoryId() == 0){
            //parentCategoryId = 0 means that the category has no parent category
            return;
        }
        Category parentCategory = categoryRepository.findById(requestCategoryDTO.getParentCategoryId()).orElse(null);
        if (parentCategory == null){
            throw new BadRequestException("Parent category with such id does not exists");
        }
    }

    private Category getCategoryIfExists(int id){
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null){
            throw new BadRequestException("The category does not exist");
        }
        return category;
    }

    public CategoryDTO addCategory(RequestCategoryDTO requestCategoryDTO) {
        validateCategoryInputData(requestCategoryDTO);
        Category category = new Category();
        category.setName(requestCategoryDTO.getName());
        category.setParentCategory(categoryRepository.findById(requestCategoryDTO.getParentCategoryId()).orElse(null));
        return new CategoryDTO(categoryRepository.save(category));
    }

    public CategoryDTO editCategory(int id, RequestCategoryDTO requestCategoryDTO) {
        Category editedCategory = getCategoryIfExists(id);
        validateCategoryInputData(requestCategoryDTO);
        editedCategory.setName(requestCategoryDTO.getName());
        editedCategory.setParentCategory(categoryRepository.findById(requestCategoryDTO.getParentCategoryId()).orElse(null));
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
        if (categoriesWithoutParent.size() == 0){
            throw new NotFoundException("No categories found");
        }
        categoriesWithoutParent.forEach(category -> categories.add(new CategoryAndSubcategoriesDTO(category)));
        return categories;
    }
}
