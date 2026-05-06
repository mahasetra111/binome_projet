package edu.hei.school.agricultural.service;

import edu.hei.school.agricultural.entity.*;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final AttendanceRepository attendanceRepository;
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public List<CollectivityActivity> createActivities(String collectivityId, List<CollectivityActivity> activities) {
        Collectivity collectivity = collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id=" + collectivityId + " not found"));

        for (CollectivityActivity activity : activities) {
            if (activity.getExecutiveDate() != null && activity.getRecurrenceRule() != null) {
                throw new BadRequestException("Cannot provide both executiveDate and recurrenceRule");
            }
            activity.setId(randomUUID().toString());
            activity.setCollectivity(collectivity);
        }
        return activityRepository.saveAll(activities);
    }

    public List<CollectivityActivity> getActivities(String collectivityId) {
        collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id=" + collectivityId + " not found"));
        return activityRepository.findAllByCollectivityId(collectivityId);
    }

    public List<ActivityAttendance> createAttendances(String collectivityId, String activityId,
                                                      List<ActivityAttendance> attendances) {
        collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id=" + collectivityId + " not found"));

        activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Activity.id=" + activityId + " not found"));

        CollectivityActivity activity = new CollectivityActivity();
        activity.setId(activityId);

        List<ActivityAttendance> toSave = new ArrayList<>();
        for (ActivityAttendance attendance : attendances) {
            // Vérifier si la présence existe déjà et n'est pas UNDEFINED
            var existing = attendanceRepository.findByActivityIdAndMemberId(activityId, attendance.getMember().getId());
            if (existing.isPresent() && !AttendanceStatus.UNDEFINED.equals(existing.get().getAttendanceStatus())) {
                throw new BadRequestException("Attendance already confirmed for member.id=" + attendance.getMember().getId());
            }
            attendance.setId(randomUUID().toString());
            attendance.setActivity(activity);
            toSave.add(attendance);
        }
        return attendanceRepository.saveAll(toSave);
    }

    public List<ActivityAttendance> getAttendances(String collectivityId, String activityId) {
        collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id=" + collectivityId + " not found"));

        activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Activity.id=" + activityId + " not found"));

        return attendanceRepository.findAllByActivityId(activityId);
    }
}