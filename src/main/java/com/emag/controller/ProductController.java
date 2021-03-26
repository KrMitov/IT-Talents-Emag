package com.emag.controller;

import com.emag.model.dto.ProductDTO;
import com.emag.model.dto.RequestProductDTO;
import com.emag.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController extends AbstractController{

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public ProductDTO addProduct(@RequestBody RequestProductDTO requestProductDTO){
        //check the session if the logged user is admin
        return productService.addProduct(requestProductDTO);
    }

    @PutMapping("/products/{id}")
    public ProductDTO editProduct(@PathVariable int id, @RequestBody RequestProductDTO requestProductDTO){
        //check the session if the logged user is admin
        return productService.editProduct(id, requestProductDTO);
    }

    @DeleteMapping("/products/{id}")
    public ProductDTO deleteProduct(@PathVariable int id){
        //check the session if the logged user is admin
        return productService.deleteProduct(id);
    }

    @GetMapping("/products/{id}")
    public ProductDTO getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }
}
