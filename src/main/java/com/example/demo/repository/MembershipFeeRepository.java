package com.example.demo.repository;

import com.example.demo.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class MembershipFeeRepository {

    public void create(UUID collectivityId, String frequency, double amount, String label) {
        try (Connection conn = DBConnection.getConnection()) {

            String sql = """
                INSERT INTO membership_fee
                (collectivity_id, frequency, amount, label, status)
                VALUES (?, ?, ?, ?, 'ACTIVE')
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, collectivityId);
            ps.setString(2, frequency);
            ps.setDouble(3, amount);
            ps.setString(4, label);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
