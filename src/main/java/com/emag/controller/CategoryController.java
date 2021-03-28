package com.emag.controller;

import com.emag.model.dto.categorydto.RequestCategoryDTO;
import com.emag.model.dto.categorydto.CategoryAndSubcategoriesDTO;
import com.emag.model.dto.categorydto.CategoryDTO;
import com.emag.model.dto.produtcdto.ProductDTO;
import com.emag.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class CategoryController extends AbstractController{

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SessionManager sessionManager;

    @GetMapping("/category/{id}")
    public List<ProductDTO> getProductsFromCategory(@PathVariable int id){
        return categoryService.getProductsFromCategory(id);
    }

    @PostMapping("/category")
    public CategoryDTO addCategory(@RequestBody RequestCategoryDTO requestCategoryDTO, HttpSession session){
        sessionManager.adminVerification(session);
        return categoryService.addCategory(requestCategoryDTO);
    }

    @PutMapping("/category/{id}")
    public CategoryDTO editCategory(@PathVariable int id, @RequestBody RequestCategoryDTO requestCategoryDTO, HttpSession session){
        sessionManager.adminVerification(session);
        return categoryService.editCategory(id, requestCategoryDTO);
    }

    @DeleteMapping("/category/{id}")
    public CategoryAndSubcategoriesDTO deleteCategory(@PathVariable int id, HttpSession session){
        sessionManager.adminVerification(session);
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/category")
    public List<CategoryAndSubcategoriesDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }
}
