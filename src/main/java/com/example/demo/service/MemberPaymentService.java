package com.example.demo.service;

import com.example.demo.dto.CreateMemberPaymentDto;
import com.example.demo.repository.MemberPaymentRepository;

import java.util.UUID;

public class MemberPaymentService {

    private final MemberPaymentRepository repository = new MemberPaymentRepository();

    public void create(String memberId, CreateMemberPaymentDto dto) {

        repository.create(
                UUID.fromString(memberId),
                dto.getAmount(),
                dto.getMembershipFeeIdentifier(),
                dto.getAccountCreditedIdentifier(),
                dto.getPaymentMode()
        );

        // 👉 ici tu ajouteras plus tard la transaction automatique
    }
}