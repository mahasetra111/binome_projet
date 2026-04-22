package com.example.demo.repository;

import com.example.demo.config.DBConnection;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.UUID;

@Repository
public class CollectivityRepository {

    private Connection getConnection() throws SQLException {
        try {
            return DBConnection.getConnection();
        } catch (Exception e) {
            throw new SQLException("Erreur de connexion : " + e.getMessage(), e);
        }
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

    /**
     * Retourne number, name, president_id, vice_president_id, treasurer_id, secretary_id, location
     * pour une collectivité donnée.
     */
    public ResultSet findRawById(UUID id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM collectivity WHERE id = ?");
        ps.setObject(1, id);
        return ps.executeQuery();
    }

    /**
     * Vérifie si le nom est déjà utilisé par une autre collectivité.
     */
    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM collectivity WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL existsByName : " + e.getMessage(), e);
        }
    }

    /**
     * Vérifie si le numéro est déjà utilisé par une autre collectivité.
     */
    public boolean existsByNumber(int number) {
        String sql = "SELECT 1 FROM collectivity WHERE number = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, number);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL existsByNumber : " + e.getMessage(), e);
        }
    }

    /**
     * Retourne number et name actuels d'une collectivité (null si pas encore attribués).
     * Format : int[0] = number (ou -1 si null), String = name
     */
    public Object[] findIdentityById(UUID id) {
        String sql = "SELECT number, name FROM collectivity WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Object number = rs.getObject("number");
                    Object name   = rs.getObject("name");
                    return new Object[]{number, name};
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL findIdentityById : " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Attribue le numéro et le nom à une collectivité.
     */
    public void assignIdentity(UUID id, int number, String name) {
        String sql = "UPDATE collectivity SET number = ?, name = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, number);
            ps.setString(2, name);
            ps.setObject(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL assignIdentity : " + e.getMessage(), e);
        }
    }

    /**
     * Retourne une collectivité complète avec ses infos pour construire la réponse.
     */
    public Object[] findFullById(UUID id) {
        String sql = "SELECT * FROM collectivity WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                            rs.getObject("id", UUID.class),
                            rs.getObject("number"),
                            rs.getString("name"),
                            rs.getString("location"),
                            rs.getObject("president_id", UUID.class),
                            rs.getObject("vice_president_id", UUID.class),
                            rs.getObject("treasurer_id", UUID.class),
                            rs.getObject("secretary_id", UUID.class)
                    };
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL findFullById : " + e.getMessage(), e);
        }
        return null;
    }
}