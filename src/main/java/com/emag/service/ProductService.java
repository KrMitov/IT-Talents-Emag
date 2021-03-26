package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dto.ProductDTO;
import com.emag.model.dto.RequestProductDTO;
import com.emag.model.pojo.Category;
import com.emag.model.pojo.Product;
import com.emag.model.repository.CategoryRepository;
import com.emag.model.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

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

    //TODO move to util class
    private void validateProductInputData(RequestProductDTO requestProductDTO){
        if (requestProductDTO.getFullName() == null || requestProductDTO.getFullName().trim().equals("")){
            throw new BadRequestException("Invalid product name");
        }
        if (requestProductDTO.getBrand() == null || requestProductDTO.getBrand().trim().equals("")){
            throw new BadRequestException("Invalid brand name");
        }
        if (requestProductDTO.getModel() == null || requestProductDTO.getModel().trim().equals("")){
            throw new BadRequestException("Invalid model name");
        }
        if (requestProductDTO.getRegularPrice() <= 0){
            throw new BadRequestException("Invalid regular price for product");
        }
        if (requestProductDTO.getDiscountedPrice() != null && requestProductDTO.getDiscountedPrice() <= 0){
            throw new BadRequestException("Invalid discounted price for product");
        }
        if (requestProductDTO.getDescription() != null && requestProductDTO.getDescription().trim().equals("")){
            requestProductDTO.setDescription(null);
        }
        if (requestProductDTO.getQuantity() <= 0){
            throw new BadRequestException("Invalid product quantity");
        }
        if (requestProductDTO.getWarrantyYears() != null && requestProductDTO.getWarrantyYears() < 0){
            throw new BadRequestException("Invalid warranty years for product");
        }
        if (categoryRepository.findById(requestProductDTO.getCategoryId()).orElse(null) == null){
            throw new BadRequestException("Invalid category id");
        }
    }

    public ProductDTO addProduct(RequestProductDTO requestProductDTO) {
        validateProductInputData(requestProductDTO);
        requestProductDTO = trimProductInputData(requestProductDTO);
        Product product = new Product(requestProductDTO);
        product.setCategory(categoryRepository.findById(requestProductDTO.getCategoryId()).get());
        return new ProductDTO(productRepository.save(product));
    }

    private Product getProductIfExists(int id){
        Product product = productRepository.findById(id).orElse(null);
        if (product == null){
            throw new BadRequestException("The product does not exist");
        }
        return product;
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
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category == null) {
                throw new BadRequestException("Invalid category id");
            }
            product.setCategory(category);
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

    public ProductDTO getProductById(int id) throws NotFoundException {
        Product foundProduct = productRepository.findById(id).orElse(null);
        if (foundProduct == null){
            throw new NotFoundException("Product not found");
        }
        if (foundProduct.getDeletedAt() != null){
            throw new BadRequestException("The product is not available!");
        }
        ProductDTO productDTO = new ProductDTO(foundProduct);
//        productDTO.setReviews(reviewService.getReviewsForProduct());
        return productDTO;
    }
}
