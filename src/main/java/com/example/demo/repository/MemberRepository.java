package com.example.demo.repository;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Gender;
import com.example.demo.model.Member;
import com.example.demo.model.MemberOccupation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.sql.Date;

import java.sql.*;
import java.util.*;

@Repository
public class MemberRepository {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // ---------------------------------------------------------------
    // LECTURE
    // ---------------------------------------------------------------

    public Member findById(UUID id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL findById : " + e.getMessage(), e);
        }
        throw new NotFoundException("Membre introuvable : " + id);
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT 1 FROM member WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL existsById : " + e.getMessage(), e);
        }
    }

    public List<Member> findAllByIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();

        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = "SELECT * FROM member WHERE id IN (" + placeholders + ")";

        List<Member> result = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < ids.size(); i++) {
                ps.setObject(i + 1, ids.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL findAllByIds : " + e.getMessage(), e);
        }
        return result;
    }

    public List<Member> findRefereesByMemberId(UUID memberId) {
        String sql = """
                SELECT m.* FROM member m
                JOIN member_referee mr ON m.id = mr.referee_id
                WHERE mr.member_id = ?
                """;
        List<Member> referees = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) referees.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL findRefereesByMemberId : " + e.getMessage(), e);
        }
        return referees;
    }

    // ---------------------------------------------------------------
    // ECRITURE
    // ---------------------------------------------------------------

    public Member save(Member member) {
        String sql = """
                INSERT INTO member
                    (id, first_name, last_name, birth_date, gender, address,
                     profession, phone_number, email, occupation, collectivity_id, joined_at)
                VALUES (gen_random_uuid(), ?, ?, ?, ?::varchar, ?, ?, ?, ?, ?::varchar, ?, CURRENT_DATE)
                RETURNING *
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, member.getFirstName());
            ps.setString(2, member.getLastName());
            ps.setDate(3, Date.valueOf(member.getBirthDate()));
            ps.setString(4, member.getGender().name());
            ps.setString(5, member.getAddress());
            ps.setString(6, member.getProfession());
            ps.setString(7, member.getPhoneNumber());
            ps.setString(8, member.getEmail());
            ps.setString(9, member.getOccupation().name());
            ps.setObject(10, member.getCollectivityId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL save member : " + e.getMessage(), e);
        }
        throw new RuntimeException("Échec de l'insertion du membre");
    }

    public void saveReferees(UUID memberId, List<UUID> refereeIds) {
        if (refereeIds == null || refereeIds.isEmpty()) return;

        String sql = "INSERT INTO member_referee (member_id, referee_id) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (UUID refereeId : refereeIds) {
                ps.setObject(1, memberId);
                ps.setObject(2, refereeId);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL saveReferees : " + e.getMessage(), e);
        }
    }

    public void updateCollectivityId(UUID memberId, UUID collectivityId) {
        String sql = "UPDATE member SET collectivity_id = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, collectivityId);
            ps.setObject(2, memberId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL updateCollectivityId : " + e.getMessage(), e);
        }
    }

    // ---------------------------------------------------------------
    // MAPPING
    // ---------------------------------------------------------------

    private Member mapRow(ResultSet rs) throws SQLException {
        Member m = new Member();
        m.setId(rs.getObject("id", UUID.class));
        m.setFirstName(rs.getString("first_name"));
        m.setLastName(rs.getString("last_name"));
        m.setBirthDate(rs.getDate("birth_date").toLocalDate());
        m.setGender(Gender.valueOf(rs.getString("gender")));
        m.setAddress(rs.getString("address"));
        m.setProfession(rs.getString("profession"));
        m.setPhoneNumber(rs.getString("phone_number"));
        m.setEmail(rs.getString("email"));
        m.setOccupation(MemberOccupation.valueOf(rs.getString("occupation")));
        m.setCollectivityId(rs.getObject("collectivity_id", UUID.class));
        Date joinedAt = rs.getDate("joined_at");
        if (joinedAt != null) m.setJoinedAt(joinedAt.toLocalDate());
        return m;
    }
}
