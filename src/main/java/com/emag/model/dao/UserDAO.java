package com.emag.model.dao;

import com.emag.util.FileCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class UserDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    FileCreator fileCreator;

    public void getUsersWhoOrdered(String date) {
        Timestamp startDate = Timestamp.valueOf(date + " 00:00:00");
        Timestamp endDate = Timestamp.valueOf(date + " 23:59:59");
        String sql = "select email,coalesce(nickname,'no available username') as nickname,o.created_at from users u join orders o " +
                "on u.id = o.user_id where o.created_at between ? and ? " +
                "group by u.id";
        StringBuilder result = new StringBuilder();
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, startDate);
            ps.setTimestamp(2, endDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.append("email: " + rs.getString("email") + ", nickname: " + rs.getString("nickname") + ", order date:" +
                        rs.getTimestamp("created_at") + "\n");
            }
            fileCreator.addTextToFile(result.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void getUsersByEmailAndProducts(String email, int warrantyYears, double minProductPrice, double maxProductPrice) {
        String sql = "select email,warranty_years,p.full_name,regular_price from " +
                "users u join orders o on u.id = o.user_id " +
                "join orders_have_products op on o.id = op.order_id " +
                "join products p on op.product_id = p.id " +
                "group by user_id " +
                "having email Like ? and p.warranty_years >= ? and p.regular_price between ? and ? " +
                "order by p.regular_price desc";
        StringBuilder result = new StringBuilder();
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + email);
            ps.setInt(2, warrantyYears);
            ps.setDouble(3, minProductPrice);
            ps.setDouble(4, maxProductPrice);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.append("email: " + rs.getString("email") + " warranty years: " + rs.getInt("warranty_years")
                        + " product_name " + rs.getString("full_name") + " product price: " + rs.getDouble("regular_price") + "\n");
            }
            fileCreator.addTextToFile(result.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void getOrdersByCity(String city, int minNumberOfOrders) {
        String sql = "select u.id,email,count(*) as orders,city from users u join saved_addresses s " +
                "on u.id = s.user_id " +
                "join addresses a " +
                "on s.address_id = a.id " +
                "join orders o " +
                "on u.id = o.user_id " +
                "group by u.id " +
                "having city = ? and orders > ?";
        StringBuilder result = new StringBuilder();
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, city);
            ps.setInt(2, minNumberOfOrders);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.append("userID: " + rs.getInt("id") + ", email: " + rs.getString("email") + ", number of orders: " +
                        rs.getInt("orders") + ", city: " + rs.getString("city") + "\n");
            }
            fileCreator.addTextToFile(result.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void printUsersWithMostOrders(int numberOfUsers) {
        String sql = " select u.id,email,count(*) as orders from users u join orders o " +
                "on u.id = o.user_id " +
                "group by u.id " +
                "order by orders desc " +
                "limit ?";
        StringBuilder result = new StringBuilder();
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, numberOfUsers);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.append("userID: " + rs.getInt("id") + " email: " + rs.getString("email") + " nubmer of orders:"
                        + rs.getInt("orders") + "\n");
            }
            fileCreator.addTextToFile(result.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
