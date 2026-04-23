package com.example.demo.service;

import com.example.demo.config.DBConnection;
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

import java.util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
    public Collectivity findById(String id) {

        Collectivity collectivity = null;

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "SELECT * FROM collectivity WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, UUID.fromString(id));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                collectivity = new Collectivity();
                collectivity.setId(UUID.fromString(rs.getString("id")));
                collectivity.setName(rs.getString("name"));
                collectivity.setNumber(rs.getInt("number"));
                collectivity.setLocation(rs.getString("location"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return collectivity;
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

        try (Connection conn = DBConnection.getConnection()) {

            String sql = """
            UPDATE collectivity
            SET name = ?, number = ?
            WHERE id = ?
        """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dto.getName());
            ps.setInt(2, dto.getNumber());
            ps.setObject(3, UUID.fromString(id));

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return findById(id);
    }

    public Collectivity getCollectivityWithMembers(String id) {

        Collectivity c = null;

        try (Connection conn = DBConnection.getConnection()) {

            // 1. collectivity
            String sql = "SELECT * FROM collectivity WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, UUID.fromString(id));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                c = new Collectivity();
                c.setId(UUID.fromString(rs.getString("id")));
                c.setName(rs.getString("name"));
                c.setNumber(rs.getInt("number"));
                c.setLocation(rs.getString("location"));
            }

            // 2. members
            String sqlMembers = "SELECT * FROM member WHERE collectivity_id = ?";
            PreparedStatement ps2 = conn.prepareStatement(sqlMembers);
            ps2.setObject(1, UUID.fromString(id));

            ResultSet rs2 = ps2.executeQuery();

            List<Member> members = new ArrayList<>();

            while (rs2.next()) {
                Member m = new Member();
                m.setId(UUID.fromString(rs2.getString("id")));
                m.setFirstName(rs2.getString("first_name"));
                m.setLastName(rs2.getString("last_name"));
                members.add(m);
            }

            if (c != null) {
                c.setMembers(members);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }
    public List<Map<String, Object>> getAccountsAtDate(String collectivityId, String at) {

        List<Map<String, Object>> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {

            String sql = """
            SELECT id, type, amount
            FROM financial_account
            WHERE id = ?
        """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, UUID.fromString(collectivityId));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Map<String, Object> acc = new HashMap<>();

                acc.put("id", rs.getObject("id"));
                acc.put("type", rs.getString("type"));
                acc.put("amount", rs.getDouble("amount"));

                // ⚠️ simplifié : version future = historique par date
                acc.put("asOfDate", at);

                result.add(acc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
