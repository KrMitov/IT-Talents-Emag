package com.emag.model.dao;

import com.emag.model.dto.produtcdto.FilterProductsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ProductDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Integer> getFilteredProductsIds(FilterProductsDTO filter) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT * FROM products WHERE deleted_at IS NULL AND ");
        StringBuilder queryParams = new StringBuilder();
        List<Integer> productsPerPageParams = new ArrayList<>();
        Integer categoryId = filter.getCategoryId();
        if (categoryId != null){
            query.append("category_id = ? AND ");
            queryParams.append(categoryId.toString()).append(",");
        }
        String keyword = filter.getSearchKeyword();
        if (keyword != null && !keyword.trim().equals("")){
            query.append("(full_name LIKE ? OR description LIKE ?) AND ");
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
        if (minPrice != null && minPrice >= 0){
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
        List<String> splitParams = new ArrayList<>(Arrays.asList(queryParams.toString().split(",")));

        try( Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query.toString())){
            int paramsCount = 0;
            if (!splitParams.isEmpty()) {
                for (int i = 0; i < splitParams.size(); i++) {
                    statement.setString(i + 1, splitParams.get(i));
                    paramsCount++;
                }
            }
            if (!productsPerPageParams.isEmpty()){
                for (Integer param : productsPerPageParams) {
                    statement.setInt(++paramsCount, param);
                }
            }
            ResultSet result = statement.executeQuery();
            List<Integer> foundProductIds = new ArrayList<>();
            while(result.next()){
                foundProductIds.add(result.getInt("id"));
            }
            return foundProductIds;
        }
    }
}
