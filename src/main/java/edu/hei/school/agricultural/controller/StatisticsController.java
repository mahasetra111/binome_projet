package edu.hei.school.agricultural.controller;

import edu.hei.school.agricultural.controller.dto.CollectivityLocalStatistics;
import edu.hei.school.agricultural.controller.dto.CollectivityOverallStatistics;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/collectivites/{id}/statistics")
    public ResponseEntity<?> getLocalStatistics(
            @PathVariable String id,
            @RequestParam String from,
            @RequestParam String to) {
        try {
            List<CollectivityLocalStatistics> statistics = statisticsService.getLocalStatistics(
                    id,
                    LocalDate.parse(from),
                    LocalDate.parse(to)
            );
            return ResponseEntity.status(OK).body(statistics);
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/collectivites/statistics")
    public ResponseEntity<?> getOverallStatistics(
            @RequestParam String from,
            @RequestParam String to) {
        try {
            List<CollectivityOverallStatistics> statistics = statisticsService.getOverallStatistics(
                    LocalDate.parse(from),
                    LocalDate.parse(to)
            );
            return ResponseEntity.status(OK).body(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}