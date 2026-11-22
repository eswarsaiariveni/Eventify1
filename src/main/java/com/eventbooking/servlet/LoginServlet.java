
package com.eventbooking.servlet;

import com.eventbooking.dao.UserDAO;
import com.eventbooking.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Get login form values
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println("========== LOGIN REQUEST ==========");
        System.out.println("Email: " + email);
        System.out.println("Password received: " + (password != null));

        // Validate input
        if (email == null || email.trim().isEmpty()
                || password == null || password.isEmpty()) {

            response.sendRedirect("login.html?error=empty");
            return;
        }

        // Check user credentials
        UserDAO userDAO = new UserDAO();
        User user = userDAO.loginUser(email.trim(), password);

        if (user != null) {

            System.out.println("Login successful!");

            // Create a session for the logged-in user
            HttpSession session = request.getSession(true);

            // Prevent session fixation
            request.changeSessionId();

            // Store the complete user object in the session
            session.setAttribute("user", user);

            // Store name and email for the profile dropdown
            session.setAttribute("userName", user.getName());
            session.setAttribute("userEmail", user.getEmail());

            // Redirect to the dashboard
            response.sendRedirect("dashboard.html");

        } else {

            System.out.println("Login failed!");

            response.sendRedirect("login.html?error=invalid");
        }
    }
}
