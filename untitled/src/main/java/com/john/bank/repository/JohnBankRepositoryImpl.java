package com.john.bank.repository;

import com.john.bank.models.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
