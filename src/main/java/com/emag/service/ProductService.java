package com.emag.service;

import com.emag.model.dto.ProductDTO;
import com.emag.model.pojo.Product;
import com.emag.model.repository.ProductRepository;
import javassist.NotFoundException;
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
        return new ProductDTO(product.get());
    }
}
