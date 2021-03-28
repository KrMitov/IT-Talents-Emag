package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.model.pojo.Category;
import com.emag.model.pojo.Product;
import com.emag.model.pojo.Review;
import com.emag.model.pojo.User;
import com.emag.model.repository.CategoryRepository;
import com.emag.model.repository.ProductRepository;
import com.emag.model.repository.ReviewRepository;
import com.emag.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractService {

    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected CategoryRepository categoryRepository;
    @Autowired
    protected ReviewRepository reviewRepository;

    protected Product getProductIfExists(int id){
        Product product = productRepository.findById(id).orElse(null);
        if (product == null){
            throw new BadRequestException("The product does not exist");
        }
        return product;
    }

    protected User getUserIfExists(int id){
        User user = userRepository.findById(id).orElse(null);
        if (user == null){
            throw new BadRequestException("The user does not exist");
        }
        return user;
    }

    protected Category getCategoryIfExists(int id){
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null){
            throw new BadRequestException("The category does not exist");
        }
        return category;
    }

    protected Category getParentCategory(Integer id){
        if (id == null || id == 0){
            return null;
        }
        return getCategoryIfExists(id);
    }

    protected Review getReview(Product product, User user){
        return reviewRepository.findByReviewedProductAndReviewer(product, user);
    }
}
