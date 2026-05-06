package edu.hei.school.agricultural.controller;

import edu.hei.school.agricultural.controller.dto.*;
import edu.hei.school.agricultural.entity.Member;
import edu.hei.school.agricultural.entity.*;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @PostMapping("/collectivities/{id}/activities")
    public ResponseEntity<?> createActivities(
            @PathVariable String id,
            @RequestBody List<CreateCollectivityActivity> dtos) {
        try {
            List<CollectivityActivity> activities = dtos.stream()
                    .map(dto -> {
                        CollectivityActivity activity = new CollectivityActivity();
                        activity.setLabel(dto.getLabel());
                        activity.setActivityType(dto.getActivityType());
                        activity.setMemberOccupationConcerned(dto.getMemberOccupationConcerned());
                        activity.setExecutiveDate(dto.getExecutiveDate());
                        if (dto.getRecurrenceRule() != null) {
                            activity.setRecurrenceRule(new MonthlyRecurrenceRule(
                                    dto.getRecurrenceRule().getWeekOrdinal(),
                                    dto.getRecurrenceRule().getDayOfWeek()
                            ));
                        }
                        return activity;
                    })
                    .toList();

            List<CollectivityActivityDto> result = activityService.createActivities(id, activities)
                    .stream()
                    .map(this::mapToDto)
                    .toList();

            return ResponseEntity.status(OK).body(result);
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/activities")
    public ResponseEntity<?> getActivities(@PathVariable String id) {
        try {
            List<CollectivityActivityDto> result = activityService.getActivities(id)
                    .stream()
                    .map(this::mapToDto)
                    .toList();
            return ResponseEntity.status(OK).body(result);
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/collectivities/{id}/activities/{activityId}/attendance")
    public ResponseEntity<?> createAttendances(
            @PathVariable String id,
            @PathVariable String activityId,
            @RequestBody List<CreateActivityMemberAttendance> dtos) {
        try {
            List<ActivityAttendance> attendances = dtos.stream()
                    .map(dto -> {
                        Member member = new Member();
                        member.setId(dto.getMemberIdentifier());
                        ActivityAttendance attendance = new ActivityAttendance();
                        attendance.setMember(member);
                        attendance.setAttendanceStatus(dto.getAttendanceStatus());
                        return attendance;
                    })
                    .toList();

            List<ActivityMemberAttendanceDto> result = activityService.createAttendances(id, activityId, attendances)
                    .stream()
                    .map(this::mapAttendanceToDto)
                    .toList();

            return ResponseEntity.status(CREATED).body(result);
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/activities/{activityId}/attendance")
    public ResponseEntity<?> getAttendances(
            @PathVariable String id,
            @PathVariable String activityId) {
        try {
            List<ActivityMemberAttendanceDto> result = activityService.getAttendances(id, activityId)
                    .stream()
                    .map(this::mapAttendanceToDto)
                    .toList();
            return ResponseEntity.status(OK).body(result);
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private CollectivityActivityDto mapToDto(CollectivityActivity activity) {
        CollectivityActivityDto dto = new CollectivityActivityDto();
        dto.setId(activity.getId());
        dto.setLabel(activity.getLabel());
        dto.setActivityType(activity.getActivityType());
        dto.setMemberOccupationConcerned(activity.getMemberOccupationConcerned());
        dto.setExecutiveDate(activity.getExecutiveDate());
        if (activity.getRecurrenceRule() != null) {
            MonthlyRecurrenceRuleDto recurrenceDto = new MonthlyRecurrenceRuleDto();
            recurrenceDto.setWeekOrdinal(activity.getRecurrenceRule().getWeekOrdinal());
            recurrenceDto.setDayOfWeek(activity.getRecurrenceRule().getDayOfWeek());
            dto.setRecurrenceRule(recurrenceDto);
        }
        return dto;
    }

    private ActivityMemberAttendanceDto mapAttendanceToDto(ActivityAttendance attendance) {
        MemberDescription memberDescription = null;
        if (attendance.getMember() != null) {
            memberDescription = new MemberDescription(
                    attendance.getMember().getId(),
                    attendance.getMember().getFirstName(),
                    attendance.getMember().getLastName(),
                    attendance.getMember().getEmail(),
                    attendance.getMember().getOccupation() != null ? attendance.getMember().getOccupation().name() : null
            );
        }
        return new ActivityMemberAttendanceDto(
                attendance.getId(),
                memberDescription,
                attendance.getAttendanceStatus()
        );
    }
}