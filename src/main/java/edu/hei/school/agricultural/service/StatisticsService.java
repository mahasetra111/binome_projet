package edu.hei.school.agricultural.service;

import edu.hei.school.agricultural.controller.dto.CollectivityInformation;
import edu.hei.school.agricultural.controller.dto.CollectivityLocalStatistics;
import edu.hei.school.agricultural.controller.dto.CollectivityOverallStatistics;
import edu.hei.school.agricultural.controller.dto.MemberDescription;
import edu.hei.school.agricultural.entity.Collectivity;
import edu.hei.school.agricultural.entity.Member;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.repository.CollectivityRepository;
import edu.hei.school.agricultural.repository.MemberRepository;
import edu.hei.school.agricultural.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public List<CollectivityLocalStatistics> getLocalStatistics(String collectivityId, LocalDate from, LocalDate to) {
        Collectivity collectivity = collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id=" + collectivityId + " not found"));

        List<Member> members = memberRepository.findAllByCollectivity(collectivity);
        Map<String, Double> earnedAmounts = statisticsRepository.getEarnedAmountByMember(collectivityId, from, to);
        Map<String, Double> unpaidAmounts = statisticsRepository.getUnpaidAmountByMember(collectivityId, from, to);
        Map<String, Double> assiduityPercentages = statisticsRepository.getAssiduityPercentageByMember(collectivityId, from, to);

        List<CollectivityLocalStatistics> statistics = new ArrayList<>();
        for (Member member : members) {
            MemberDescription memberDescription = new MemberDescription(
                    member.getId(),
                    member.getFirstName(),
                    member.getLastName(),
                    member.getEmail(),
                    member.getOccupation() != null ? member.getOccupation().name() : null
            );
            Double earned = earnedAmounts.getOrDefault(member.getId(), 0.0);
            Double unpaid = unpaidAmounts.getOrDefault(member.getId(), 0.0);
            Double assiduity = assiduityPercentages.getOrDefault(member.getId(), 0.0);
            statistics.add(new CollectivityLocalStatistics(memberDescription, earned, unpaid, assiduity));
        }
        return statistics;
    }
    public List<CollectivityOverallStatistics> getOverallStatistics(LocalDate from, LocalDate to) {
        Map<String, Integer> newMembersCount = statisticsRepository.getNewMembersCountByCollectivity(from, to);
        Map<String, Double> upToDatePercentage = statisticsRepository.getUpToDatePercentageByCollectivity(from, to);
        Map<String, Double> overallAssiduity = statisticsRepository.getOverallAssiduityPercentageByCollectivity(from, to);

        List<CollectivityOverallStatistics> statistics = new ArrayList<>();
        for (String collectivityId : upToDatePercentage.keySet()) {
            Collectivity collectivity = collectivityRepository.findById(collectivityId).orElse(null);
            if (collectivity == null) continue;

            CollectivityInformation info = new CollectivityInformation();
            info.setName(collectivity.getName());
            info.setNumber(collectivity.getNumber());

            statistics.add(new CollectivityOverallStatistics(
                    info,
                    newMembersCount.getOrDefault(collectivityId, 0),
                    upToDatePercentage.get(collectivityId),
                    overallAssiduity.getOrDefault(collectivityId, 0.0)
            ));
        }
        return statistics;
    }
}