package com.roxana.salonoop.repository;

import com.roxana.salonoop.connection.ConnectionFactory;
import com.roxana.salonoop.model.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRepository {

    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Service s = new Service(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration_minutes"),
                        rs.getDouble("price")
                );
                services.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }

}
