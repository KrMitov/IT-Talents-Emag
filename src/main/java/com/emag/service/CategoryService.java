package com.emag.service;

import com.emag.model.dto.ProductDTO;
import com.emag.model.pojo.Category;
import com.emag.model.repository.CategoryRepository;
import javassist.NotFoundException;
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
        List<ProductDTO> productsDTOList = new ArrayList<>();
        category.get().getProducts().forEach(product -> productsDTOList.add(new ProductDTO(product)));
        return productsDTOList;
    }
}
