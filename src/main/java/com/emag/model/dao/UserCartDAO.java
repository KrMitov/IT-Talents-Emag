package com.emag.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;


@Component
public class UserCartDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void removeProductFromCart(int productId,int userId) throws SQLException {
        String sql = " delete from user_carts where user_id = ? AND product_id = ?";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,userId);
            ps.setInt(2,productId);
            ps.executeUpdate();
        }
    }

}
