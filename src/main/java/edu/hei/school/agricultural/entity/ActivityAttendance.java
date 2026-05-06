package edu.hei.school.agricultural.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityAttendance {
    private String id;
    private CollectivityActivity activity;
    private Member member;
    private AttendanceStatus attendanceStatus;
}