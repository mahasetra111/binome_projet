package com.example.demo.repository;

import com.example.demo.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

public class MemberPaymentRepository {

    public void create(UUID memberId, int amount, String feeId, String accountId, String mode) {
        try (Connection conn = DBConnection.getConnection()) {

            String sql = """
                INSERT INTO member_payment
                (member_id, amount, membership_fee_id, account_id, payment_mode)
                VALUES (?, ?, ?, ?, ?)
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, memberId);
            ps.setInt(2, amount);
            ps.setString(3, feeId);
            ps.setString(4, accountId);
            ps.setString(5, mode);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}