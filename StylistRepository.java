package com.roxana.salonoop.repository;

import com.roxana.salonoop.connection.ConnectionFactory;
import com.roxana.salonoop.model.Stylist;
import com.roxana.salonoop.model.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StylistRepository {

    public Stylist findByUserId(int userId) {

        String sql = """
        SELECT id, name, specialization
        FROM stylists
        WHERE user_id = ?
    """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Stylist(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("specialization")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<Stylist> getStylistsForService(int serviceId) {
        List<Stylist> stylists = new ArrayList<>();

        String sql = """
            SELECT s.id, s.name, s.specialization
            FROM stylists s
            JOIN stylist_services ss ON s.id = ss.stylist_id
            WHERE ss.service_id = ?
        """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, serviceId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                stylists.add(new Stylist(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("specialization")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stylists;
    }

    public void createStylist(String name, String specialization) {

        String sql = "INSERT INTO stylists (name, specialization) VALUES (?, ?)";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, specialization);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createStylistWithServices(
            Stylist stylist,
            List<Service> services
    ) {
        String insertStylistSql =
                "INSERT INTO stylists (name, specialization) VALUES (?, ?)";

        String linkSql =
                "INSERT INTO stylist_services (stylist_id, service_id) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            int stylistId;
            try (PreparedStatement stmt =
                         conn.prepareStatement(insertStylistSql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, stylist.getName());
                stmt.setString(2, stylist.getSpecialization());
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (!rs.next()) {
                    throw new SQLException("Failed to retrieve stylist ID");
                }
                stylistId = rs.getInt(1);
            }

            try (PreparedStatement linkStmt =
                         conn.prepareStatement(linkSql)) {
                for (Service s : services) {
                    linkStmt.setInt(1, stylistId);
                    linkStmt.setInt(2, s.getId());
                    linkStmt.addBatch();
                }
                linkStmt.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not create stylist with services");
        }
    }

}
