package com.example.demo.controller;

import com.example.demo.dto.CreateMemberPaymentDto;
import com.example.demo.service.MemberPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberPaymentController {

    private final MemberPaymentService service = new MemberPaymentService();

    @PostMapping("/{id}/payments")
    public ResponseEntity<String> create(
            @PathVariable String id,
            @RequestBody CreateMemberPaymentDto dto) {

        service.create(id, dto);
        return ResponseEntity.ok("Payment created");
    }
}