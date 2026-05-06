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
public class ActivityRepository {
    private final Connection connection;

    public List<CollectivityActivity> saveAll(List<CollectivityActivity> activities) {
        List<CollectivityActivity> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("""
                INSERT INTO collectivity_activity (id, label, activity_type, collectivity_id, executive_date, recurrence_week_ordinal, recurrence_day_of_week)
                VALUES (?, ?, ?::activity_type, ?, ?, ?, ?)
                ON CONFLICT (id) DO UPDATE SET label = excluded.label
                """)) {
            for (CollectivityActivity activity : activities) {
                ps.setString(1, activity.getId());
                ps.setString(2, activity.getLabel());
                ps.setString(3, activity.getActivityType().name());
                ps.setString(4, activity.getCollectivity().getId());
                if (activity.getExecutiveDate() != null) {
                    ps.setDate(5, Date.valueOf(activity.getExecutiveDate()));
                } else {
                    ps.setNull(5, Types.DATE);
                }
                if (activity.getRecurrenceRule() != null) {
                    ps.setInt(6, activity.getRecurrenceRule().getWeekOrdinal());
                    ps.setString(7, activity.getRecurrenceRule().getDayOfWeek());
                } else {
                    ps.setNull(6, Types.INTEGER);
                    ps.setNull(7, Types.VARCHAR);
                }
                ps.addBatch();
            }
            ps.executeBatch();

            for (CollectivityActivity activity : activities) {
                saveOccupationsConcerned(activity);
                result.add(findById(activity.getId()).orElseThrow());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private void saveOccupationsConcerned(CollectivityActivity activity) {
        if (activity.getMemberOccupationConcerned() == null) return;
        try (PreparedStatement ps = connection.prepareStatement("""
                INSERT INTO activity_occupation_concerned (id, activity_id, occupation)
                VALUES (?, ?, ?)
                ON CONFLICT DO NOTHING
                """)) {
            for (MemberOccupation occupation : activity.getMemberOccupationConcerned()) {
                ps.setString(1, randomUUID().toString());
                ps.setString(2, activity.getId());
                ps.setString(3, occupation.name());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<CollectivityActivity> findById(String id) {
        try (PreparedStatement ps = connection.prepareStatement("""
                SELECT id, label, activity_type, collectivity_id, executive_date, recurrence_week_ordinal, recurrence_day_of_week
                FROM collectivity_activity WHERE id = ?
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

    public List<CollectivityActivity> findAllByCollectivityId(String collectivityId) {
        List<CollectivityActivity> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("""
                SELECT id, label, activity_type, collectivity_id, executive_date, recurrence_week_ordinal, recurrence_day_of_week
                FROM collectivity_activity WHERE collectivity_id = ?
                """)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private CollectivityActivity mapFromResultSet(ResultSet rs) throws SQLException {
        CollectivityActivity activity = new CollectivityActivity();
        activity.setId(rs.getString("id"));
        activity.setLabel(rs.getString("label"));
        activity.setActivityType(ActivityType.valueOf(rs.getString("activity_type")));

        Date execDate = rs.getDate("executive_date");
        if (execDate != null) {
            activity.setExecutiveDate(execDate.toLocalDate());
        }

        int weekOrdinal = rs.getInt("recurrence_week_ordinal");
        String dayOfWeek = rs.getString("recurrence_day_of_week");
        if (!rs.wasNull() && dayOfWeek != null) {
            activity.setRecurrenceRule(new MonthlyRecurrenceRule(weekOrdinal, dayOfWeek));
        }

        activity.setMemberOccupationConcerned(findOccupationsByActivityId(activity.getId()));

        Collectivity collectivity = new Collectivity();
        collectivity.setId(rs.getString("collectivity_id"));
        activity.setCollectivity(collectivity);

        return activity;
    }

    private List<MemberOccupation> findOccupationsByActivityId(String activityId) {
        List<MemberOccupation> occupations = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("""
                SELECT occupation FROM activity_occupation_concerned WHERE activity_id = ?
                """)) {
            ps.setString(1, activityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                occupations.add(MemberOccupation.valueOf(rs.getString("occupation")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return occupations;
    }
}