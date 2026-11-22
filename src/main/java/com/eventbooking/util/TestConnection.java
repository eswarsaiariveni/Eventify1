package com.eventbooking.util;

public class TestConnection {

    public static void main(String[] args) {

        String password = System.getenv("DB_PASSWORD");

        if (password == null) {
            System.out.println("❌ DB_PASSWORD is NULL");
        } else if (password.isEmpty()) {
            System.out.println("❌ DB_PASSWORD is EMPTY");
        } else {
            System.out.println("✅ DB_PASSWORD is available");
        }

        try {
            var connection = DBConnection.getConnection();

            System.out.println("================================");
            System.out.println("MySQL Connection Successful!");
            System.out.println("Database: eventdb");
            System.out.println("================================");

            connection.close();

        } catch (Exception e) {
            System.out.println("MySQL Connection Failed!");
            e.printStackTrace();
        }
    }
}