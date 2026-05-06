package edu.hei.school.agricultural.controller.dto;

import edu.hei.school.agricultural.entity.ActivityType;
import edu.hei.school.agricultural.entity.MemberOccupation;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateCollectivityActivity {
    private String label;
    private ActivityType activityType;
    private List<MemberOccupation> memberOccupationConcerned;
    private MonthlyRecurrenceRuleDto recurrenceRule;
    private LocalDate executiveDate;
}