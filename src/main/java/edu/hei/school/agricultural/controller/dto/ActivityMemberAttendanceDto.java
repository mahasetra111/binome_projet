package edu.hei.school.agricultural.controller.dto;

import edu.hei.school.agricultural.entity.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityMemberAttendanceDto {
    private String id;
    private MemberDescription memberDescription;
    private AttendanceStatus attendanceStatus;
}