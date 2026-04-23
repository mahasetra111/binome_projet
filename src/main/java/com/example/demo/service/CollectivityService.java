package com.example.demo.service;

import com.example.demo.dto.CollectivityInformation;
import com.example.demo.dto.CreateCollectivityDto;
import com.example.demo.dto.CreateCollectivityStructureDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Collectivity;
import com.example.demo.model.CollectivityStructure;
import com.example.demo.model.Member;
import com.example.demo.repository.CollectivityRepository;
import com.example.demo.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CollectivityService {

    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public CollectivityService(CollectivityRepository collectivityRepository,
                               MemberRepository memberRepository) {
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
    }
    public List<Collectivity> findAll() {
        return collectivityRepository.findAll();
    }

    public List<Collectivity> getAllCollectivities() {
        return collectivityRepository.findAll();
    }

    public List<Collectivity> createCollectivities(List<CreateCollectivityDto> dtos) {
        List<Collectivity> result = new ArrayList<>();
        for (CreateCollectivityDto dto : dtos) {
            result.add(createSingleCollectivity(dto));
        }
        return result;
    }

    private Collectivity createSingleCollectivity(CreateCollectivityDto dto) {

        // 1. Approbation de la fédération obligatoire
        if (!Boolean.TRUE.equals(dto.getFederationApproval())) {
            throw new BadRequestException(
                    "L'autorisation formelle de la fédération est requise (federationApproval = true).");
        }

        // 2. Structure obligatoire
        CreateCollectivityStructureDto structureDto = dto.getStructure();
        if (structureDto == null) {
            throw new BadRequestException(
                    "La structure de la collectivité est obligatoire (president, vicePresident, treasurer, secretary).");
        }
        validateStructureFields(structureDto);

        // 3. Résoudre les 4 membres de la structure
        Member president     = resolveMember(structureDto.getPresident(),     "president");
        Member vicePresident = resolveMember(structureDto.getVicePresident(), "vicePresident");
        Member treasurer     = resolveMember(structureDto.getTreasurer(),     "treasurer");
        Member secretary     = resolveMember(structureDto.getSecretary(),     "secretary");

        // 4. Résoudre tous les membres de la liste
        List<UUID> memberIds = new ArrayList<>();
        if (dto.getMembers() != null) {
            for (String memberIdStr : dto.getMembers()) {
                UUID memberId = parseUUID(memberIdStr, "member");
                if (!memberRepository.existsById(memberId)) {
                    throw new NotFoundException("Membre introuvable : " + memberIdStr);
                }
                memberIds.add(memberId);
            }
        }

        // 5. Sauvegarder la collectivité
        UUID collectivityId = collectivityRepository.save(
                dto.getLocation(),
                true,
                president.getId(),
                vicePresident.getId(),
                treasurer.getId(),
                secretary.getId()
        );

        // 6. Rattacher tous les membres à cette collectivité
        for (UUID memberId : memberIds) {
            memberRepository.updateCollectivityId(memberId, collectivityId);
        }

        // Rattacher aussi les membres de la structure s'ils ne sont pas dans la liste
        for (UUID structureMemberId : List.of(
                president.getId(), vicePresident.getId(),
                treasurer.getId(), secretary.getId())) {
            if (!memberIds.contains(structureMemberId)) {
                memberRepository.updateCollectivityId(structureMemberId, collectivityId);
            }
        }

        // 7. Recharger les membres depuis la base pour la réponse
        List<Member> allMembers = memberRepository.findAllByIds(memberIds);
        president     = memberRepository.findById(president.getId());
        vicePresident = memberRepository.findById(vicePresident.getId());
        treasurer     = memberRepository.findById(treasurer.getId());
        secretary     = memberRepository.findById(secretary.getId());

        // 8. Construire la réponse
        CollectivityStructure structure = new CollectivityStructure();
        structure.setPresident(president);
        structure.setVicePresident(vicePresident);
        structure.setTreasurer(treasurer);
        structure.setSecretary(secretary);

        Collectivity collectivity = new Collectivity();
        collectivity.setId(collectivityId);
        collectivity.setLocation(dto.getLocation());
        collectivity.setStructure(structure);
        collectivity.setMembers(allMembers);

        return collectivity;
    }

    private void validateStructureFields(CreateCollectivityStructureDto s) {
        if (s.getPresident() == null || s.getPresident().isBlank()) {
            throw new BadRequestException("Le président de la structure est obligatoire.");
        }
        if (s.getVicePresident() == null || s.getVicePresident().isBlank()) {
            throw new BadRequestException("Le vice-président de la structure est obligatoire.");
        }
        if (s.getTreasurer() == null || s.getTreasurer().isBlank()) {
            throw new BadRequestException("Le trésorier de la structure est obligatoire.");
        }
        if (s.getSecretary() == null || s.getSecretary().isBlank()) {
            throw new BadRequestException("Le secrétaire de la structure est obligatoire.");
        }
    }

    private Member resolveMember(String idStr, String role) {
        UUID id = parseUUID(idStr, role);
        return memberRepository.findById(id);
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
    public Collectivity update(String id, CollectivityInformation dto) {

        UUID uuid = UUID.fromString(id);

        // ✔ Vérification
        if (!collectivityRepository.existsById(uuid)) {
            throw new RuntimeException("Collectivity not found");
        }

        // ✔ Update
        collectivityRepository.updateCollectivity(uuid, dto.getName(), dto.getNumber());

        // ✔ Retourner la collectivité
        return collectivityRepository.findById(uuid);
    }

}
