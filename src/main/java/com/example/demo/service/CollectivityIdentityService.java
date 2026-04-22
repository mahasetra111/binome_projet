package com.example.demo.service;


import com.example.demo.dto.AssignCollectivityIdentityDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Collectivity;
import com.example.demo.model.CollectivityStructure;
import com.example.demo.model.Member;
import com.example.demo.repository.CollectivityRepository;
import com.example.demo.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CollectivityIdentityService {

    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public CollectivityIdentityService(CollectivityRepository collectivityRepository,
                                       MemberRepository memberRepository) {
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
    }

    public Collectivity assignIdentity(UUID collectivityId, AssignCollectivityIdentityDto dto) {

        // 1. Vérifier que la collectivité existe
        if (!collectivityRepository.existsById(collectivityId)) {
            throw new NotFoundException("Collectivité introuvable : " + collectivityId);
        }

        // 2. Vérifier les champs obligatoires
        if (dto.getNumber() == null) {
            throw new BadRequestException("Le numéro est obligatoire.");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new BadRequestException("Le nom est obligatoire.");
        }

        // 3. Vérifier si numéro ou nom déjà attribués à CETTE collectivité
        Object[] currentIdentity = collectivityRepository.findIdentityById(collectivityId);
        if (currentIdentity != null) {
            Object currentNumber = currentIdentity[0];
            Object currentName   = currentIdentity[1];

            if (currentNumber != null) {
                throw new BadRequestException(
                        "Le numéro a déjà été attribué à cette collectivité et ne peut plus être modifié.");
            }
            if (currentName != null) {
                throw new BadRequestException(
                        "Le nom a déjà été attribué à cette collectivité et ne peut plus être modifié.");
            }
        }

        // 4. Vérifier unicité du nom (409 si déjà utilisé par une autre collectivité)
        if (collectivityRepository.existsByName(dto.getName())) {
            throw new ConflictException(
                    "Le nom '" + dto.getName() + "' est déjà utilisé par une autre collectivité.");
        }

        // 5. Vérifier unicité du numéro (409 si déjà utilisé par une autre collectivité)
        if (collectivityRepository.existsByNumber(dto.getNumber())) {
            throw new ConflictException(
                    "Le numéro '" + dto.getNumber() + "' est déjà utilisé par une autre collectivité.");
        }

        // 6. Attribuer le numéro et le nom
        collectivityRepository.assignIdentity(collectivityId, dto.getNumber(), dto.getName());

        // 7. Construire et retourner la collectivité complète
        return buildCollectivity(collectivityId);
    }

    private Collectivity buildCollectivity(UUID collectivityId) {
        Object[] row = collectivityRepository.findFullById(collectivityId);
        if (row == null) throw new NotFoundException("Collectivité introuvable : " + collectivityId);

        UUID presidentId     = (UUID) row[4];
        UUID vicePresidentId = (UUID) row[5];
        UUID treasurerId     = (UUID) row[6];
        UUID secretaryId     = (UUID) row[7];

        Member president     = memberRepository.findById(presidentId);
        Member vicePresident = memberRepository.findById(vicePresidentId);
        Member treasurer     = memberRepository.findById(treasurerId);
        Member secretary     = memberRepository.findById(secretaryId);

        CollectivityStructure structure = new CollectivityStructure();
        structure.setPresident(president);
        structure.setVicePresident(vicePresident);
        structure.setTreasurer(treasurer);
        structure.setSecretary(secretary);

        Collectivity collectivity = new Collectivity();
        collectivity.setId((UUID) row[0]);
        collectivity.setNumber(row[1] != null ? (Integer) row[1] : null);
        collectivity.setName((String) row[2]);
        collectivity.setLocation((String) row[3]);
        collectivity.setStructure(structure);

        return collectivity;
    }
}