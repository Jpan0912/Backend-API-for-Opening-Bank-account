package com.john.bank.repository;

import com.john.bank.models.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Repository
public class JohnBankRepositoryImpl implements JohnBankRepository{

    private final DataSource dataSource;

    public JohnBankRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addCustomer(Customer customer) {
        String sql = "INSERT INTO users (name, password) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getPassword());
            statement.executeUpdate();

        } catch (SQLException e) {
            // TODO Add more robust logging with log4j
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer getCustomerByName(String name) {
        String sql = "SELECT * FROM users WHERE name =?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getLong("id"));
                customer.setName(rs.getString("name"));
                customer.setPassword(rs.getString("password"));
                return customer;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving customer", e);
        }
    }
}

