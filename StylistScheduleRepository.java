package com.roxana.salonoop.repository;

import com.roxana.salonoop.connection.ConnectionFactory;
import java.sql.*;
import java.time.LocalTime;

public class StylistScheduleRepository {

    public LocalTime[] getWorkingHours(int stylistId, int dayOfWeek) {

        String sql = """
            SELECT start_time, end_time
            FROM stylist_schedule
            WHERE stylist_id = ? AND day_of_week = ?
        """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, stylistId);
            stmt.setInt(2, dayOfWeek);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new LocalTime[]{
                        rs.getTime("start_time").toLocalTime(),
                        rs.getTime("end_time").toLocalTime()
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
