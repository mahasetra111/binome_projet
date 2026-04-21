package com.example.demo.service;

import com.example.demo.dto.CreateMemberDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Member;
import com.example.demo.model.MemberOccupation;
import com.example.demo.repository.CollectivityRepository;
import com.example.demo.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final CollectivityRepository collectivityRepository;

    public MemberService(MemberRepository memberRepository,
                         CollectivityRepository collectivityRepository) {
        this.memberRepository = memberRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<Member> createMembers(List<CreateMemberDto> dtos) {
        List<Member> created = new ArrayList<>();
        for (CreateMemberDto dto : dtos) {
            created.add(createSingleMember(dto));
        }
        return created;
    }

    private Member createSingleMember(CreateMemberDto dto) {

        // 1. Vérifier les paiements
        if (!Boolean.TRUE.equals(dto.getRegistrationFeePaid())) {
            throw new BadRequestException(
                    "Le frais d'adhésion de 50 000 Ar doit être réglé (registrationFeePaid = true).");
        }
        if (!Boolean.TRUE.equals(dto.getMembershipDuesPaid())) {
            throw new BadRequestException(
                    "Les cotisations annuelles doivent être réglées (membershipDuesPaid = true).");
        }

        // 2. Vérifier que la collectivité cible existe
        UUID collectivityId = parseUUID(dto.getCollectivityIdentifier(), "collectivityIdentifier");
        if (!collectivityRepository.existsById(collectivityId)) {
            throw new NotFoundException("Collectivité introuvable : " + dto.getCollectivityIdentifier());
        }

        // 3. Vérifier les parrains (au moins 2)
        List<String> refereeIds = dto.getReferees();
        if (refereeIds == null || refereeIds.size() < 2) {
            throw new BadRequestException("Le candidat doit avoir au moins 2 parrains confirmés.");
        }

        // 4. Récupérer les membres-parrains
        List<Member> referees = new ArrayList<>();
        for (String refereeIdStr : refereeIds) {
            UUID refereeId = parseUUID(refereeIdStr, "referee");
            Member referee = memberRepository.findById(refereeId);
            referees.add(referee);
        }

        // 5. Vérifier que tous les parrains sont confirmés (pas JUNIOR)
        for (Member referee : referees) {
            if (MemberOccupation.JUNIOR.equals(referee.getOccupation())) {
                throw new BadRequestException(
                        "Le parrain " + referee.getId() + " est JUNIOR : seuls les membres confirmés peuvent parrainer.");
            }
        }

        // 6. Règle B-2 : parrains internes >= parrains externes
        long internalCount = referees.stream()
                .filter(r -> collectivityId.equals(r.getCollectivityId()))
                .count();
        long externalCount = referees.size() - internalCount;

        if (internalCount < externalCount) {
            throw new BadRequestException(
                    "Le nombre de parrains de la collectivité cible (" + internalCount +
                            ") doit être >= au nombre de parrains extérieurs (" + externalCount + ").");
        }

        // 7. Construire et sauvegarder le nouveau membre
        Member newMember = new Member();
        newMember.setFirstName(dto.getFirstName());
        newMember.setLastName(dto.getLastName());
        newMember.setBirthDate(dto.getBirthDate());
        newMember.setGender(dto.getGender());
        newMember.setAddress(dto.getAddress());
        newMember.setProfession(dto.getProfession());
        newMember.setPhoneNumber(dto.getPhoneNumber());
        newMember.setEmail(dto.getEmail());
        newMember.setOccupation(MemberOccupation.JUNIOR);
        newMember.setCollectivityId(collectivityId);

        Member saved = memberRepository.save(newMember);

        // 8. Sauvegarder les relations de parrainage
        List<UUID> refereeUUIDs = referees.stream().map(Member::getId).toList();
        memberRepository.saveReferees(saved.getId(), refereeUUIDs);

        saved.setReferees(referees);
        return saved;
    }

    private UUID parseUUID(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException("Le champ '" + fieldName + "' est obligatoire.");
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("UUID invalide pour '" + fieldName + "' : " + value);
        }
    }
}