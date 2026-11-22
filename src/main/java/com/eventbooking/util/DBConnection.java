package com.eventbooking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/eventdb?useSSL=false&serverTimezone=Asia/Kolkata";

    private static final String USER = "root";

    private static final String PASSWORD = "MR.ESWARsai@9";

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL JDBC Driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL JDBC Driver NOT found!");
            throw new SQLException("MySQL JDBC Driver not found", e);
        }

        Connection connection =
                DriverManager.getConnection(URL, USER, PASSWORD);

        System.out.println("✅ MySQL Connection Successful!");
        System.out.println("Database: eventdb");

        return connection;
    }
}