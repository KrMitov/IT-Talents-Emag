package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dto.ProductDTO;
import com.emag.model.pojo.Product;
import com.emag.model.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductDTO getProductById(int id) throws NotFoundException {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()){
            throw new NotFoundException("Product not found");
        }
        Product foundProduct = product.get();
        if (foundProduct.getDeleted_at() != null){
            throw new BadRequestException("The product is not available!");
        }
        ProductDTO productDTO = new ProductDTO(foundProduct);
//        productDTO.setReviews(reviewService.getReviewsForProduct());
        return productDTO;
    }
}
