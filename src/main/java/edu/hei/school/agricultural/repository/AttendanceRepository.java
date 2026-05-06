package edu.hei.school.agricultural.repository;

import edu.hei.school.agricultural.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;

@Repository
@RequiredArgsConstructor
public class AttendanceRepository {
    private final Connection connection;
    private final MemberRepository memberRepository;

    public List<ActivityAttendance> saveAll(List<ActivityAttendance> attendances) {
        List<ActivityAttendance> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("""
                INSERT INTO activity_attendance (id, activity_id, member_id, attendance_status)
                VALUES (?, ?, ?, ?::attendance_status)
                ON CONFLICT (id) DO NOTHING
                """)) {
            for (ActivityAttendance attendance : attendances) {
                ps.setString(1, attendance.getId());
                ps.setString(2, attendance.getActivity().getId());
                ps.setString(3, attendance.getMember().getId());
                ps.setString(4, attendance.getAttendanceStatus().name());
                ps.addBatch();
            }
            ps.executeBatch();
            for (ActivityAttendance attendance : attendances) {
                result.add(findById(attendance.getId()).orElseThrow());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Optional<ActivityAttendance> findById(String id) {
        try (PreparedStatement ps = connection.prepareStatement("""
                SELECT id, activity_id, member_id, attendance_status
                FROM activity_attendance WHERE id = ?
                """)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public List<ActivityAttendance> findAllByActivityId(String activityId) {
        List<ActivityAttendance> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("""
                SELECT id, activity_id, member_id, attendance_status
                FROM activity_attendance WHERE activity_id = ?
                """)) {
            ps.setString(1, activityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Optional<ActivityAttendance> findByActivityIdAndMemberId(String activityId, String memberId) {
        try (PreparedStatement ps = connection.prepareStatement("""
                SELECT id, activity_id, member_id, attendance_status
                FROM activity_attendance WHERE activity_id = ? AND member_id = ?
                """)) {
            ps.setString(1, activityId);
            ps.setString(2, memberId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private ActivityAttendance mapFromResultSet(ResultSet rs) throws SQLException {
        ActivityAttendance attendance = new ActivityAttendance();
        attendance.setId(rs.getString("id"));
        attendance.setAttendanceStatus(AttendanceStatus.valueOf(rs.getString("attendance_status")));

        Member member = memberRepository.findById(rs.getString("member_id")).orElse(null);
        attendance.setMember(member);

        CollectivityActivity activity = new CollectivityActivity();
        activity.setId(rs.getString("activity_id"));
        attendance.setActivity(activity);

        return attendance;
    }
}