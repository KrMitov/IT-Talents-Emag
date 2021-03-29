package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dao.ProductDAO;
import com.emag.model.dto.produtcdto.*;
import com.emag.model.dto.reviewdto.ReviewDTO;
import com.emag.model.pojo.Product;
import com.emag.model.pojo.User;
import com.emag.service.validatorservice.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class ProductService extends AbstractService{

    @Autowired
    private ProductDAO productDAO;

    //TODO move to util class
    private RequestProductDTO trimProductInputData(RequestProductDTO requestProductDTO){
        requestProductDTO.setFullName(requestProductDTO.getFullName().trim());
        requestProductDTO.setBrand(requestProductDTO.getBrand().trim());
        requestProductDTO.setModel(requestProductDTO.getModel().trim());
        if (requestProductDTO.getDescription() != null) {
            requestProductDTO.setDescription(requestProductDTO.getDescription().trim());
        }
        return requestProductDTO;
    }

    public ProductDTO addProduct(RequestProductDTO requestProductDTO) {
        ProductValidator.validateProductInputData(requestProductDTO);
        requestProductDTO = trimProductInputData(requestProductDTO);
        Product product = new Product(requestProductDTO);
        product.setCategory(getCategoryIfExists(requestProductDTO.getCategoryId()));
        return new ProductDTO(productRepository.save(product));
    }

    //move to util class?
    //better method name?
    private Product setProductChanges(Product product, RequestProductDTO requestProductDTO){
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
        Double regularPrice = requestProductDTO.getRegularPrice();
        if (regularPrice != null && regularPrice > 0){
            product.setRegularPrice(regularPrice);
        }
        Double discountedPrice = requestProductDTO.getDiscountedPrice();
        if (discountedPrice != null && discountedPrice > 0){
            product.setDiscountedPrice(discountedPrice);
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
        return product;
    }

    public ProductDTO editProduct(int id, RequestProductDTO requestProductDTO){
        Product editedProduct = getProductIfExists(id);
        editedProduct = setProductChanges(editedProduct, requestProductDTO);
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
        StringBuilder query = new StringBuilder("SELECT * FROM products WHERE deleted_at IS NULL AND ");
        StringBuilder queryParams = new StringBuilder();
        List<Integer> productsPerPageParams = new ArrayList<>();
        Integer categoryId = filter.getCategoryId();
        if (categoryId != null && getCategoryIfExists(categoryId) != null){
            query.append("category_id = ? AND ");
            queryParams.append(categoryId.toString()).append(",");
        }
        String keyword = filter.getSearchKeyword();
        if (keyword != null && !keyword.trim().equals("")){
            query.append("full_name LIKE ? OR description LIKE ? AND ");
            queryParams.append("%").append(keyword).append("%").append(",");
            queryParams.append("%").append(keyword).append("%").append(",");
        }
        String brand = filter.getBrand();
        if (brand != null && !brand.trim().equals("")){
            query.append("brand LIKE ? AND ");
            queryParams.append("%").append(brand).append("%").append(",");
        }
        String model = filter.getModel();
        if (model != null && !model.trim().equals("")){
            query.append("model LIKE ? AND ");
            queryParams.append("%").append(model).append("%").append(",");
        }
        Double maxPrice = filter.getMaxPrice();
        if (maxPrice != null && maxPrice > 0){
            query.append("IF(discounted_price IS NOT NULL, discounted_price, regular_price) <= ? AND ");
            queryParams.append(maxPrice.toString()).append(",");
        }
        Double minPrice = filter.getMinPrice();
        if (minPrice != null && minPrice > 0){
            query.append("IF(discounted_price IS NOT NULL, discounted_price, regular_price) >= ? AND ");
            queryParams.append(minPrice.toString()).append(",");
        }
        Boolean discountedOnly = filter.getDiscountedOnly();
        if (discountedOnly != null && discountedOnly){
            query.append("discounted_price IS NOT NULL ");
        }
        //removes the last AND if it is not necessary
        if (query.substring(query.length() - 4).equals("AND ")){
            query = new StringBuilder(query.substring(0, query.length() - 4));
        }
        Boolean orderByPrice = filter.getOrderByPrice();
        if (orderByPrice != null && orderByPrice){
            query.append("ORDER BY IF(discounted_price IS NOT NULL, discounted_price, regular_price) ");
            Boolean sortDesc = filter.getSortDesc();
            if (sortDesc != null && sortDesc){
                query.append("DESC ");
            }
        }
        Integer productsPerPage = filter.getProductsPerPage();
        if (productsPerPage != null && productsPerPage >= 0){
            query.append("LIMIT ? ");
            productsPerPageParams.add(productsPerPage);
            Integer pageNumber = filter.getPageNumber();
            if (pageNumber != null && pageNumber > 0){
                query.append("OFFSET ?");
                int offset = (pageNumber - 1) * productsPerPage;
                productsPerPageParams.add(offset);
            }
        }
        query.append(";");
        List<String> params = new ArrayList<>(Arrays.asList(queryParams.toString().split(",")));
        List<ProductDTO> products = new ArrayList<>();
        productDAO.getFilteredProductsIds(query.toString(), params, productsPerPageParams)
                .forEach(id -> products.add(new ProductDTO(getProductIfExists(id))));
        return products;
    }


    public LikedProductsForUserDTO makeProductFavourite(FavouriteProductDTO favouriteProductDTO) {
        User user =  getUserIfExists(favouriteProductDTO.getUserId());
        Product product = getProductIfExists(favouriteProductDTO.getProductId());
        List<Product> likedProducts = user.getLikedProducts();
        if (likedProducts.contains(product)){
            throw new BadRequestException("User has already liked this product");
        }
        likedProducts.add(product);
        user.setLikedProducts(likedProducts);
        return new LikedProductsForUserDTO(userRepository.save(user));
    }

    public LikedProductsForUserDTO removeFavouriteProduct(FavouriteProductDTO favouriteProductDTO) {
        User user =  getUserIfExists(favouriteProductDTO.getUserId());
        Product product = getProductIfExists(favouriteProductDTO.getProductId());
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
