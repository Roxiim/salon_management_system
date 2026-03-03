package com.roxana.salonoop.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = "jdbc:postgresql://localhost:5432/salon_oop";
    private static final String USER = "postgres";
    private static final String PASS = "3@gPEf";

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (SQLException e) {
            System.err.println("ERROR CONNECTING TO DATABASE:");
            e.printStackTrace();
        }
        return connection;
    }

}
