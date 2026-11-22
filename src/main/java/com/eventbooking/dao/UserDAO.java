package com.eventbooking.dao;

import com.eventbooking.model.User;
import com.eventbooking.util.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    // =========================
    // REGISTER USER
    // =========================
    public boolean registerUser(User user) {

        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            String hashedPassword = BCrypt.hashpw(
                    user.getPassword(),
                    BCrypt.gensalt()
            );

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, hashedPassword);
            ps.setString(4, "USER");

            int rows = ps.executeUpdate();

            System.out.println("Registration rows inserted: " + rows);

            return rows > 0;

        } catch (Exception e) {

            System.out.println("===== REGISTRATION ERROR =====");
            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // LOGIN USER
    // =========================
    public User loginUser(String email, String password) {

        String sql = "SELECT * FROM users WHERE email = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String storedHash = rs.getString("password");

                if (BCrypt.checkpw(password, storedHash)) {

                    User user = new User();

                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(storedHash);
                    user.setRole(rs.getString("role"));

                    return user;
                }
            }

        } catch (Exception e) {

            System.out.println("===== LOGIN ERROR =====");
            e.printStackTrace();
        }

        return null;
    }
}