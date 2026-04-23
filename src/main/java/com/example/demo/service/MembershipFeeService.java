package com.example.demo.service;

import com.example.demo.dto.CreateMembershipFeeDto;
import com.example.demo.repository.MembershipFeeRepository;

import java.util.UUID;

public class MembershipFeeService {

    private final MembershipFeeRepository repository = new MembershipFeeRepository();

    public void create(String collectivityId, CreateMembershipFeeDto dto) {

        if (dto.getAmount() <= 0) {
            throw new RuntimeException("Amount must be > 0");
        }

        repository.create(
                UUID.fromString(collectivityId),
                dto.getFrequency(),
                dto.getAmount(),
                dto.getLabel()
        );
    }
}