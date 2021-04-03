package com.emag.model.dao;

import com.emag.exceptions.BadRequestException;
import com.emag.model.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserOrderDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void insertOrderedQuantity(int orderId, HashMap<Product,Integer> productQuantities){
        String sql = "update orders_have_products set quantity = ? where order_id = ? and product_id =?";
        try(Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            for (Map.Entry<Product, Integer> entry : productQuantities.entrySet()) {
                ps.setInt(1,entry.getValue());
                ps.setInt(2,orderId);
                ps.setInt(3,entry.getKey().getId());
                int numberOfRows = ps.executeUpdate();
                if(numberOfRows == 0){
                    throw new BadRequestException("quantity could not be updated");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int getProductQuantity(int orderId,int productId){
        int quantity = 0;
        String sql = "select quantity from orders_have_products where order_id = ? and product_id = ?";
        try(Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,orderId);
            ps.setInt(2,productId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            quantity = rs.getInt("quantity");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return quantity;
    }

}
