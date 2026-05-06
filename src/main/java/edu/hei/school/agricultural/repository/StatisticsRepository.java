package edu.hei.school.agricultural.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class StatisticsRepository {
    private final Connection connection;

    // Montant encaissé par membre pour une collectivité sur une période
    public Map<String, Double> getEarnedAmountByMember(String collectivityId, LocalDate from, LocalDate to) {
        Map<String, Double> result = new LinkedHashMap<>();
        try (PreparedStatement ps = connection.prepareStatement("""
                SELECT cm.member_id, COALESCE(SUM(mp.amount), 0) as earned
                FROM collectivity_member cm
                LEFT JOIN member_payment mp ON mp.member_id = cm.member_id
                    AND mp.creation_date BETWEEN ? AND ?
                WHERE cm.collectivity_id = ?
                GROUP BY cm.member_id
                """)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ps.setString(3, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("member_id"), rs.getDouble("earned"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // Montant impayé potentiel par membre (cotisations ACTIVE uniquement)
    public Map<String, Double> getUnpaidAmountByMember(String collectivityId, LocalDate from, LocalDate to) {
        Map<String, Double> result = new LinkedHashMap<>();
        try (PreparedStatement ps = connection.prepareStatement("""
                SELECT cm.member_id,
                       COALESCE(SUM(mf.amount), 0) - COALESCE(SUM(mp.amount), 0) as unpaid
                FROM collectivity_member cm
                JOIN membership_fee mf ON mf.collectivity_id = cm.collectivity_id
                    AND mf.status = 'ACTIVE'
                    AND mf.eligible_from BETWEEN ? AND ?
                LEFT JOIN member_payment mp ON mp.member_id = cm.member_id
                    AND mp.membership_fee_id = mf.id
                WHERE cm.collectivity_id = ?
                GROUP BY cm.member_id
                """)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ps.setString(3, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double unpaid = rs.getDouble("unpaid");
                result.put(rs.getString("member_id"), Math.max(unpaid, 0));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // Nombre de nouveaux membres par collectivité sur une période
    public Map<String, Integer> getNewMembersCountByCollectivity(LocalDate from, LocalDate to) {
        Map<String, Integer> result = new LinkedHashMap<>();
        try (PreparedStatement ps = connection.prepareStatement("""
                SELECT collectivity_id, COUNT(*) as new_members
                FROM collectivity_member
                WHERE joined_at BETWEEN ? AND ?
                GROUP BY collectivity_id
                """)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("collectivity_id"), rs.getInt("new_members"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // Pourcentage de membres à jour par collectivité
    public Map<String, Double> getUpToDatePercentageByCollectivity(LocalDate from, LocalDate to) {
        Map<String, Double> result = new LinkedHashMap<>();
        try (PreparedStatement ps = connection.prepareStatement("""
                SELECT cm.collectivity_id,
                       COUNT(DISTINCT cm.member_id) as total_members,
                       COUNT(DISTINCT mp.member_id) as paid_members
                FROM collectivity_member cm
                JOIN membership_fee mf ON mf.collectivity_id = cm.collectivity_id
                    AND mf.status = 'ACTIVE'
                    AND mf.eligible_from BETWEEN ? AND ?
                LEFT JOIN member_payment mp ON mp.member_id = cm.member_id
                    AND mp.membership_fee_id = mf.id
                GROUP BY cm.collectivity_id
                """)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int total = rs.getInt("total_members");
                int paid = rs.getInt("paid_members");
                double percentage = total > 0 ? (paid * 100.0 / total) : 0.0;
                result.put(rs.getString("collectivity_id"), percentage);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    // Taux d'assiduité par membre pour une collectivité sur une période
    public Map<String, Double> getAssiduityPercentageByMember(String collectivityId, LocalDate from, LocalDate to) {
        Map<String, Double> result = new LinkedHashMap<>();
        try (PreparedStatement ps = connection.prepareStatement("""
            SELECT cm.member_id,
                   COUNT(aa.id) as total_activities,
                   COUNT(CASE WHEN aa.attendance_status = 'ATTENDED' THEN 1 END) as attended
            FROM collectivity_member cm
            JOIN collectivity_activity ca ON ca.collectivity_id = cm.collectivity_id
                AND ca.executive_date BETWEEN ? AND ?
            LEFT JOIN activity_attendance aa ON aa.activity_id = ca.id
                AND aa.member_id = cm.member_id
            WHERE cm.collectivity_id = ?
            GROUP BY cm.member_id
            """)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ps.setString(3, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int total = rs.getInt("total_activities");
                int attended = rs.getInt("attended");
                double percentage = total > 0 ? (attended * 100.0 / total) : 0.0;
                result.put(rs.getString("member_id"), percentage);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // Taux d'assiduité global par collectivité
    public Map<String, Double> getOverallAssiduityPercentageByCollectivity(LocalDate from, LocalDate to) {
        Map<String, Double> result = new LinkedHashMap<>();
        try (PreparedStatement ps = connection.prepareStatement("""
            SELECT cm.collectivity_id,
                   COUNT(aa.id) as total,
                   COUNT(CASE WHEN aa.attendance_status = 'ATTENDED' THEN 1 END) as attended
            FROM collectivity_member cm
            JOIN collectivity_activity ca ON ca.collectivity_id = cm.collectivity_id
                AND ca.executive_date BETWEEN ? AND ?
            LEFT JOIN activity_attendance aa ON aa.activity_id = ca.id
                AND aa.member_id = cm.member_id
            GROUP BY cm.collectivity_id
            """)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int total = rs.getInt("total");
                int attended = rs.getInt("attended");
                double percentage = total > 0 ? (attended * 100.0 / total) : 0.0;
                result.put(rs.getString("collectivity_id"), percentage);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}