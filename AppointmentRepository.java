package com.roxana.salonoop.repository;

import com.roxana.salonoop.connection.ConnectionFactory;
import com.roxana.salonoop.model.Appointment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;

public class AppointmentRepository {

    public boolean createAppointment(int customerId, int stylistId, int serviceId, Timestamp dateTime) {

        String sql = """
            INSERT INTO appointments (customer_id, stylist_id, service_id, appointment_datetime)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, stylistId);
            stmt.setInt(3, serviceId);
            stmt.setTimestamp(4, dateTime);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("ERROR: Appointment not created → " + e.getMessage());
            return false;
        }
    }

    public List<String> getBusyHours(int stylistId, LocalDate date) {

        String sql = """
        SELECT a.appointment_datetime, s.duration_minutes
        FROM appointments a
        JOIN services s ON a.service_id = s.id
        WHERE a.stylist_id = ?
          AND DATE(a.appointment_datetime) = ?
    """;

        List<String> busyHours = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, stylistId);
            stmt.setDate(2, Date.valueOf(date));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LocalDateTime start = rs.getTimestamp("appointment_datetime").toLocalDateTime();
                int duration = rs.getInt("duration_minutes");
                int slots = duration / 30;

                for (int i = 0; i < slots; i++) {
                    String hour = start.plusMinutes(i * 30)
                            .toLocalTime()
                            .toString()
                            .substring(0, 5);
                    busyHours.add(hour);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return busyHours;
    }

    public List<Appointment> getAppointmentsForCustomer(int customerId) {

        String sql = """
        SELECT a.id,
               a.customer_id,
               a.stylist_id,
               a.service_id,
               a.appointment_datetime,
               a.status,
               s.name AS service_name,
               st.name AS stylist_name
        FROM appointments a
        JOIN services s ON a.service_id = s.id
        JOIN stylists st ON a.stylist_id = st.id
        WHERE a.customer_id = ?
        ORDER BY a.appointment_datetime
    """;

        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customerId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Appointment a = new Appointment();
                a.setId(rs.getInt("id"));
                a.setCustomerId(rs.getInt("customer_id"));
                a.setStylistId(rs.getInt("stylist_id"));
                a.setServiceId(rs.getInt("service_id"));
                a.setAppointmentDateTime(
                        rs.getTimestamp("appointment_datetime").toLocalDateTime()
                );
                a.setStatus(rs.getString("status"));

                a.setServiceName(rs.getString("service_name"));
                a.setStylistName(rs.getString("stylist_name"));

                appointments.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public boolean cancelAppointment(int appointmentId, int customerId) {

        String sql = """
        UPDATE appointments
        SET status = 'CANCELLED'
        WHERE id = ? AND customer_id = ?
    """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, appointmentId);
            stmt.setInt(2, customerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Appointment> getAppointmentsForStylist(int stylistId) {

        String sql = """
        SELECT a.id,
               a.appointment_datetime,
               a.status,
               s.name AS service_name,
               u.username AS customer_name
        FROM appointments a
        JOIN services s ON a.service_id = s.id
        JOIN users u ON a.customer_id = u.id
        WHERE a.stylist_id = ?
        ORDER BY a.appointment_datetime
    """;

        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, stylistId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Appointment a = new Appointment();
                a.setId(rs.getInt("id"));
                a.setAppointmentDateTime(
                        rs.getTimestamp("appointment_datetime").toLocalDateTime()
                );
                a.setStatus(rs.getString("status"));
                a.setServiceName(rs.getString("service_name"));
                a.setCustomerName(rs.getString("customer_name"));

                appointments.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public List<Appointment> findByDateAndStylist(LocalDate date, int stylistId) {

        List<Appointment> appointments = new ArrayList<>();

        String sql = """
        SELECT a.id,
               a.appointment_datetime,
               a.status,
               s.name AS service_name,
               u.username AS customer_name
        FROM appointments a
        JOIN services s ON a.service_id = s.id
        JOIN users u ON a.customer_id = u.id
        WHERE a.stylist_id = ?
          AND a.appointment_datetime >= ?
          AND a.appointment_datetime < ?
        ORDER BY a.appointment_datetime
    """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, stylistId);
            stmt.setTimestamp(2, Timestamp.valueOf(date.atStartOfDay()));
            stmt.setTimestamp(3, Timestamp.valueOf(date.plusDays(1).atStartOfDay()));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Appointment a = new Appointment();
                a.setId(rs.getInt("id"));
                a.setAppointmentDateTime(
                        rs.getTimestamp("appointment_datetime").toLocalDateTime()
                );
                a.setStatus(rs.getString("status"));
                a.setServiceName(rs.getString("service_name"));
                a.setCustomerName(rs.getString("customer_name"));
                appointments.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

}