package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dao.ProductDAO;
import com.emag.model.dto.produtcdto.*;
import com.emag.model.dto.reviewdto.ReviewDTO;
import com.emag.model.pojo.Product;
import com.emag.model.pojo.User;
import com.emag.util.ProductValidator;
import com.emag.util.EmailService;
import com.emag.util.ProductUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class ProductService extends AbstractService{

    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private EmailService emailService;

    public ProductDTO addProduct(RequestProductDTO requestProductDTO) {
        ProductValidator.validateProductInputData(requestProductDTO);
        requestProductDTO = ProductUtility.trimProductInputData(requestProductDTO);
        Product product = new Product(requestProductDTO);
        product.setCategory(getCategoryIfExists(requestProductDTO.getCategoryId()));
        return new ProductDTO(productRepository.save(product));
    }

    private Product validateAndSetProductChanges(Product product, RequestProductDTO requestProductDTO){
        String fullName = requestProductDTO.getFullName();
        if (fullName != null && !fullName.trim().equals("")){
            product.setFullName(fullName.trim());
        }
        String brand = requestProductDTO.getBrand();
        if (brand != null && !brand.trim().equals("")){
            product.setBrand(brand.trim());
        }
        String model = requestProductDTO.getModel();
        if (model != null && !model.trim().equals("")){
            product.setModel(model.trim());
        }
        boolean priceExceptionFlag = false;
        boolean priceEmailAlert = false;
        Double newRegularPrice = requestProductDTO.getRegularPrice();
        if (newRegularPrice != null && newRegularPrice > 0){
            if (newRegularPrice <= product.getDiscountedPrice()){
                priceExceptionFlag = true;
            }
            if (newRegularPrice < product.getRegularPrice()) {
                priceEmailAlert = true;
            }
            product.setRegularPrice(newRegularPrice);
        }
        Double newDiscountedPrice = requestProductDTO.getDiscountedPrice();
        if (newDiscountedPrice != null && newDiscountedPrice > 0){
            priceExceptionFlag = newDiscountedPrice >= product.getRegularPrice();
            priceEmailAlert = newDiscountedPrice < product.getDiscountedPrice();
            product.setDiscountedPrice(newDiscountedPrice);
        }
        if (priceExceptionFlag) {
            throw new BadRequestException("Discounted price should be lower than regular price");
        }
        String description = requestProductDTO.getDescription();
        if (description != null && !description.trim().equals("")){
            product.setDescription(description.trim());
        }
        Integer quantity = requestProductDTO.getQuantity();
        if (quantity != null && quantity >= 0){
            product.setQuantity(quantity);
            product.setDeletedAt(quantity == 0 ? LocalDateTime.now() : null);
        }
        Integer warrantyYears = requestProductDTO.getWarrantyYears();
        if (warrantyYears != null && warrantyYears >= 0){
            product.setWarrantyYears(warrantyYears);
        }
        Integer categoryId = requestProductDTO.getCategoryId();
        if (categoryId != null) {
            product.setCategory(getCategoryIfExists(categoryId));
        }
        if (priceEmailAlert) {
            emailService.sendMessage(product);
        }
        return product;
    }

    public ProductDTO editProduct(int id, RequestProductDTO requestProductDTO){
        Product editedProduct = getProductIfExists(id);
        editedProduct = validateAndSetProductChanges(editedProduct, requestProductDTO);
        return new ProductDTO(productRepository.save(editedProduct));
    }

    public ProductDTO deleteProduct(int id){
        Product product = getProductIfExists(id);
        productRepository.deleteById(id);
        return new ProductDTO(product);
    }

    public ProductDTO getProductById(int id){
        Product foundProduct;
        try {
            foundProduct = getProductIfExists(id);
        }
        catch (BadRequestException e){
            throw new NotFoundException("Product not found");
        }
        if (foundProduct.getDeletedAt() != null){
            throw new BadRequestException("The product is not available!");
        }
        return new ProductDTO(foundProduct);
    }

    public List<ProductDTO> searchProductsByKeyword(String keywordSequence) {
        HashSet<Product> foundProducts = new HashSet<>();
        String[] splitKeywords = keywordSequence.trim().split("\\s+");
        for (String keyword : splitKeywords) {
            foundProducts.addAll(
                    productRepository.
                            findByFullNameContainingOrDescriptionContaining(keyword, keyword)
            );
        }
        if (foundProducts.isEmpty()){
            throw new NotFoundException("No products found");
        }
        List<ProductDTO> foundProductsDTOs = new ArrayList<>();
        foundProducts.forEach(product -> foundProductsDTOs.add(new ProductDTO(product)));
        return foundProductsDTOs;
    }

    public List<ProductDTO> filterProducts(FilterProductsDTO filter) throws SQLException {
        if (filter.getCategoryId() != null) {
            //check if category exists
            getCategoryIfExists(filter.getCategoryId());
        }
        List<ProductDTO> products = new ArrayList<>();
        productDAO.getFilteredProductsIds(filter)
                .forEach(id -> products.add(new ProductDTO(getProductIfExists(id))));
        return products;
    }


    public LikedProductsForUserDTO makeProductFavourite(int productId, int userId) {
        User user =  getUserIfExists(userId);
        Product product = getProductIfExists(productId);
        List<Product> likedProducts = user.getLikedProducts();
        if (likedProducts.contains(product)){
            throw new BadRequestException("User has already liked this product");
        }
        likedProducts.add(product);
        user.setLikedProducts(likedProducts);
        return new LikedProductsForUserDTO(userRepository.save(user));
    }

    public LikedProductsForUserDTO removeFavouriteProduct(int productId, int userId) {
        User user =  getUserIfExists(userId);
        Product product = getProductIfExists(productId);
        List<Product> likedProducts = user.getLikedProducts();
        if (!likedProducts.contains(product)){
            throw new BadRequestException("User does not like this product");
        }
        likedProducts.remove(product);
        user.setLikedProducts(likedProducts);
        return new LikedProductsForUserDTO(userRepository.save(user));
    }

    public List<ReviewDTO> getAllReviewsForProduct(int id) {
        List<ReviewDTO> reviews = new ArrayList<>();
        getProductIfExists(id).getReviews().forEach(review -> reviews.add(new ReviewDTO(review)));
        if (reviews.isEmpty()) {
            throw new NotFoundException("No reviews for this product");
        }
        return reviews;
    }
}
