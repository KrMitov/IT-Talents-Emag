package com.emag.controller;

import com.emag.model.dto.categoryDTO.RequestCategoryDTO;
import com.emag.model.dto.categoryDTO.CategoryAndSubcategoriesDTO;
import com.emag.model.dto.categoryDTO.CategoryDTO;
import com.emag.model.dto.produtcDTO.ProductDTO;
import com.emag.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController extends AbstractController{

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category/{id}")
    public List<ProductDTO> getProductsFroCategory(@PathVariable int id){
        return categoryService.getProductsFroCategory(id);
    }

    @PostMapping("/category")
    public CategoryDTO addCategory(@RequestBody RequestCategoryDTO requestCategoryDTO){
        //check the session if the logged user is admin
        return categoryService.addCategory(requestCategoryDTO);
    }

    @PutMapping("/category/{id}")
    public CategoryDTO editCategory(@PathVariable int id, @RequestBody RequestCategoryDTO requestCategoryDTO){
        //check the session if the logged user is admin
        return categoryService.editCategory(id, requestCategoryDTO);
    }

    @DeleteMapping("/category/{id}")
    public CategoryAndSubcategoriesDTO deleteCategory(@PathVariable int id){
        //check the session if the logged user is admin
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/category")
    public List<CategoryAndSubcategoriesDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }
}
