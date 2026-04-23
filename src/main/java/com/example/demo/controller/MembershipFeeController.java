package com.example.demo.controller;

import com.example.demo.dto.CreateMembershipFeeDto;
import com.example.demo.service.MembershipFeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collectivities")
public class MembershipFeeController {

    private final MembershipFeeService service = new MembershipFeeService();

    @PostMapping("/{id}/membershipFees")
    public ResponseEntity<String> create(
            @PathVariable String id,
            @RequestBody CreateMembershipFeeDto dto) {

        service.create(id, dto);
        return ResponseEntity.ok("Membership fee created");
    }
}