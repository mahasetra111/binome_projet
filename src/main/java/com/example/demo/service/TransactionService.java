package com.example.demo.service;

import com.example.demo.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class TransactionService {

    public List<Map<String, Object>> getTransactions(String collectivityId, String from, String to) {

        List<Map<String, Object>> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {

            String sql = """
                SELECT *
                FROM collectivity_transaction
                WHERE collectivity_id = ?
                AND creation_date BETWEEN ? AND ?
                ORDER BY creation_date DESC
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, UUID.fromString(collectivityId));
            ps.setDate(2, java.sql.Date.valueOf(from));
            ps.setDate(3, java.sql.Date.valueOf(to));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> tx = new HashMap<>();

                tx.put("id", rs.getObject("id"));
                tx.put("amount", rs.getDouble("amount"));
                tx.put("paymentMode", rs.getString("payment_mode"));
                tx.put("creationDate", rs.getDate("creation_date"));

                list.add(tx);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}