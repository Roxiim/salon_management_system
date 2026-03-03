package com.roxana.salonoop.repository;
import com.roxana.salonoop.util.PasswordUtil;

import com.roxana.salonoop.connection.ConnectionFactory;
import com.roxana.salonoop.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public boolean createUser(String username, String rawPassword, String role) {

        String sql = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)";

        String hashedPassword = PasswordUtil.hashPassword(rawPassword);

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, role);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User login(String username, String rawPassword) {

        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                String storedHash = rs.getString("password_hash");

                if (PasswordUtil.verifyPassword(rawPassword, storedHash)) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            storedHash,
                            rs.getString("role")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("role")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

}
