package com.eventbooking.servlet;

import com.eventbooking.dao.UserDAO;
import com.eventbooking.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println("========== REGISTER REQUEST ==========");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Password received: " + (password != null));
        System.out.println("======================================");

        try {

            User user = new User(name, email, password);

            UserDAO userDAO = new UserDAO();

            boolean registered = userDAO.registerUser(user);

            if (registered) {

                System.out.println("Registration successful!");

                response.sendRedirect("login.html");

            } else {

                showError(response, "Registration was not completed.");

            }

        } catch (Exception e) {

            System.out.println("========== REGISTRATION EXCEPTION ==========");
            e.printStackTrace();
            System.out.println("============================================");

            showError(
                    response,
                    "Registration failed. Check IntelliJ console for the exact error."
            );
        }
    }

    private void showError(
            HttpServletResponse response,
            String message) throws IOException {

        response.setContentType("text/html");

        response.getWriter().println("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Registration Error</title>
                </head>

                <body>

                    <h1>Registration Error</h1>

                    <p>%s</p>

                    <a href="register.html">Try Again</a>

                </body>
                </html>
                """.formatted(message));
    }
}