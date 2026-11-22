
package com.eventbooking.servlet;

import com.eventbooking.dao.BookingDAO;
import com.eventbooking.model.Booking;
import com.eventbooking.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/book-event")
public class BookingServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");

        HttpSession session = request.getSession(false);

        if (session == null || !(session.getAttribute("user") instanceof User)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Please log in first.\"}"
            );
            return;
        }

        User user = (User) session.getAttribute("user");

        String eventName = request.getParameter("eventName");
        String eventDate = request.getParameter("eventDate");
        String quantityText = request.getParameter("ticketQuantity");
        String priceText = request.getParameter("ticketPrice");

        if (eventName == null || eventName.trim().isEmpty()
                || eventName.trim().length() > 200) {
            sendError(response, 400, "Please provide a valid event name.");
            return;
        }

        int quantity;
        double price;

        try {
            quantity = Integer.parseInt(quantityText);
            price = Double.parseDouble(priceText);

            if (quantity < 1 || quantity > 10
                    || !Double.isFinite(price)
                    || price < 0 || price > 1000000) {
                sendError(response, 400,
                        "Choose 1–10 tickets and a valid price.");
                return;
            }

            if (eventDate != null && !eventDate.trim().isEmpty()) {
                LocalDate.parse(eventDate);
            }
        } catch (Exception e) {
            sendError(response, 400, "Invalid booking details.");
            return;
        }

        Booking booking = new Booking();
        booking.setUserEmail(user.getEmail());
        booking.setEventName(eventName.trim());
        booking.setEventDate(
                eventDate == null || eventDate.trim().isEmpty()
                        ? null : eventDate
        );
        booking.setTicketQuantity(quantity);
        booking.setTicketPrice(price);
        booking.setTotalAmount(quantity * price);

        BookingDAO bookingDAO = new BookingDAO();

        if (bookingDAO.createBooking(booking)) {
            response.getWriter().write(
                    "{\"success\":true,"
                            + "\"message\":\"Booking confirmed!\","
                            + "\"bookingReference\":\""
                            + booking.getBookingReference() + "\","
                            + "\"totalAmount\":" + booking.getTotalAmount()
                            + "}"
            );
        } else {
            sendError(response, 500,
                    "Could not save your booking. Please try again.");
        }
    }

    private void sendError(HttpServletResponse response,
                           int status, String message) throws IOException {
        response.setStatus(status);
        String safeMessage = message.replace("\\", "\\\\")
                .replace("\"", "\\\"");
        response.getWriter().write(
                "{\"success\":false,\"message\":\"" + safeMessage + "\"}"
        );
    }
}
