package com.emag.controller;

import com.emag.model.dto.produtcdto.*;
import com.emag.model.dto.reviewdto.ReviewDTO;
import com.emag.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class ProductController extends AbstractController{

    @Autowired
    private ProductService productService;
    @Autowired
    private SessionManager sessionManager;

    @PostMapping("/products")
    public ProductDTO addProduct(@RequestBody RequestProductDTO requestProductDTO, HttpSession session){
        sessionManager.adminVerification(session);
        return productService.addProduct(requestProductDTO);
    }

    @PutMapping("/products/{id}")
    public ProductDTO editProduct(@PathVariable int id, @RequestBody RequestProductDTO requestProductDTO, HttpSession session){
        sessionManager.adminVerification(session);
        return productService.editProduct(id, requestProductDTO);
    }

    @DeleteMapping("/products/{id}")
    public ProductDTO deleteProduct(@PathVariable int id, HttpSession session){
        sessionManager.adminVerification(session);
        return productService.deleteProduct(id);
    }

    @GetMapping("/products/{id}")
    public ProductDTO getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @GetMapping("/products/search/{keywordSequence}")
    public List<ProductDTO> searchProductsByKeyword(@PathVariable String keywordSequence){
        return productService.searchProductsByKeyword(keywordSequence);
    }

    @PostMapping("/products/filter")
    public List<ProductDTO> filterProducts(@RequestBody FilterProductsDTO filterProductsDTO) throws SQLException {
        return productService.filterProducts(filterProductsDTO);
    }

    @PostMapping("/products/favourite")
    public LikedProductsForUserDTO makeProductFavourite(@RequestBody FavouriteProductDTO favouriteProductDTO){
        return productService.makeProductFavourite(favouriteProductDTO);
    }

    @DeleteMapping("/products/favourite")
    public LikedProductsForUserDTO removeFavouriteProduct(@RequestBody FavouriteProductDTO favouriteProductDTO){
        return productService.removeFavouriteProduct(favouriteProductDTO);
    }

    @GetMapping("/products/{id}/reviews")
    public List<ReviewDTO> getAllReviewsForProduct(@PathVariable int id){
        return productService.getAllReviewsForProduct(id);
    }
}
