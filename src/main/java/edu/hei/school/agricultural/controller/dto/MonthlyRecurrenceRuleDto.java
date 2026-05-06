package edu.hei.school.agricultural.controller.dto;

import lombok.Data;

@Data
public class MonthlyRecurrenceRuleDto {
    private Integer weekOrdinal;
    private String dayOfWeek;
}