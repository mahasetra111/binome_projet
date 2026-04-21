package com.example.demo.repository;

import com.example.demo.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.UUID;

@Repository
public class CollectivityRepository {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public UUID save(String location,
                     boolean federationApproval,
                     UUID presidentId,
                     UUID vicePresidentId,
                     UUID treasurerId,
                     UUID secretaryId) {

        String sql = """
                INSERT INTO collectivity
                    (id, location, federation_approval,
                     president_id, vice_president_id, treasurer_id, secretary_id, created_at)
                VALUES (gen_random_uuid(), ?, ?, ?, ?, ?, ?, CURRENT_DATE)
                RETURNING id
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, location);
            ps.setBoolean(2, federationApproval);
            ps.setObject(3, presidentId);
            ps.setObject(4, vicePresidentId);
            ps.setObject(5, treasurerId);
            ps.setObject(6, secretaryId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getObject("id", UUID.class);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL save collectivity : " + e.getMessage(), e);
        }
        throw new RuntimeException("Échec de l'insertion de la collectivité");
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT 1 FROM collectivity WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL existsById collectivity : " + e.getMessage(), e);
        }
    }
}
