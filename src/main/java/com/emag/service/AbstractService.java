package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.pojo.*;
import com.emag.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Optional;

public abstract class AbstractService {

    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected CategoryRepository categoryRepository;
    @Autowired
    protected ReviewRepository reviewRepository;
    @Autowired
    protected ProductImageRepository productImageRepository;
    @Autowired
    protected RoleRepository roleRepository;
    @Autowired
    protected AddressRepository addressRepository;
    @Autowired
    protected UserImageRepository userImageRepository;
    @Autowired
    protected CouponRepository couponRepository;
    @Autowired
    protected CartRepository cartRepository;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected OrderedProductsRepository orderedProductsRepository;
    @Value("${file.path}")
    protected String filePath;
    protected static final String[] ACCEPTED_IMAGE_MIME_TYPES = {"image/png", "image/jpeg"};

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

    protected Review getReviewIfExists(int id){
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null){
            throw new BadRequestException("The review does not exist");
        }
        return review;
    }

    protected ProductImage getProductImageIfExists(int id){
        ProductImage productImage = productImageRepository.findById(id).orElse(null);
        if (productImage == null){
            throw new BadRequestException("Product image with id " + id + " does not exist");
        }
        return productImage;
    }

    protected  Role getRoleIfExists(int id){
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null){
            throw new BadRequestException("Role does not exist");
        }
        return role;
    }

    protected LocalDateTime validateDate(String date){
        try {
            LocalDateTime checkDate = LocalDateTime.parse(date);
            return checkDate;
        }catch (Exception e){
            throw new BadRequestException("Date is not valid");
        }
    }

    protected Coupon findCouponById(int id){
        Optional<Coupon> couponFromDb = couponRepository.findById(id);
        if(couponFromDb.isEmpty()){
            throw new NotFoundException("Coupon not found");
        }
        return couponFromDb.get();
    }
}
