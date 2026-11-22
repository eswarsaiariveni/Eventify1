
package com.eventbooking.dao;

import com.eventbooking.model.Booking;
import com.eventbooking.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

public class BookingDAO {

    public boolean createBooking(Booking booking) {
        String sql = "INSERT INTO bookings "
                + "(booking_reference, user_email, event_name, event_date, "
                + "ticket_quantity, ticket_price, total_amount, booking_status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String reference = "EVT-" +
                UUID.randomUUID().toString()
                        .replace("-", "")
                        .substring(0, 12)
                        .toUpperCase();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, reference);
            ps.setString(2, booking.getUserEmail());
            ps.setString(3, booking.getEventName());

            if (booking.getEventDate() == null
                    || booking.getEventDate().trim().isEmpty()) {
                ps.setNull(4, java.sql.Types.DATE);
            } else {
                ps.setDate(4, Date.valueOf(booking.getEventDate()));
            }

            ps.setInt(5, booking.getTicketQuantity());
            ps.setDouble(6, booking.getTicketPrice());

            double total = booking.getTicketQuantity()
                    * booking.getTicketPrice();
            ps.setDouble(7, total);
            ps.setString(8, "CONFIRMED");

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        booking.setBookingId(rs.getInt(1));
                    }
                }

                booking.setBookingReference(reference);
                booking.setTotalAmount(total);
                booking.setBookingStatus("CONFIRMED");
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
