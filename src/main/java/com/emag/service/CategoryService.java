package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dto.ProductDTO;
import com.emag.model.pojo.Category;
import com.emag.model.pojo.Product;
import com.emag.model.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ProductDTO> getProductsFroCategory(int id) throws NotFoundException {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()){
            throw new NotFoundException("Category not found");
        }
        List<Product> productsForCategory = category.get().getProducts();
        if (productsForCategory.size() == 0){
            throw new BadRequestException("No products for category");
        }
        List<ProductDTO> productsDTOList = new ArrayList<>();
        productsForCategory.forEach(product -> productsDTOList.add(new ProductDTO(product)));
        return productsDTOList;
    }
}
