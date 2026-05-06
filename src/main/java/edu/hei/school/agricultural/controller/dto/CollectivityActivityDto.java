package edu.hei.school.agricultural.controller.dto;

import edu.hei.school.agricultural.entity.ActivityType;
import edu.hei.school.agricultural.entity.MemberOccupation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectivityActivityDto {
    private String id;
    private String label;
    private ActivityType activityType;
    private List<MemberOccupation> memberOccupationConcerned;
    private MonthlyRecurrenceRuleDto recurrenceRule;
    private LocalDate executiveDate;
}