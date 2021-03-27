package com.emag.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Integer> getFilteredProductsIds(String query, List<String> queryParams, List<Integer> productsPerPageParams) throws SQLException {
        try( Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            int paramsCount = 0;
            if (!queryParams.isEmpty()) {
                for (int i = 0; i < queryParams.size(); i++) {
                    statement.setString(i + 1, queryParams.get(i));
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
