package com.emag.controller;

import com.emag.model.dto.ProductDTO;
import com.emag.service.ProductService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products/{id}")
    public ProductDTO getProductById(@PathVariable int id) throws NotFoundException {
        return productService.getProductById(id);
    }
}
