package com.emag.controller;

import com.emag.model.dto.ProductDTO;
import com.emag.service.CategoryService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category/{id}")
    public List<ProductDTO> getProductsFroCategory(@PathVariable int id) throws NotFoundException {
        return categoryService.getProductsFroCategory(id);
    }

}
