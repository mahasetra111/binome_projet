
package com.example.demo.controller;

import com.example.demo.dto.CreateMemberDto;
import com.example.demo.model.Member;
import com.example.demo.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<Member> getAll() {
        return memberService.findAll();
    }
    @PostMapping
    public ResponseEntity<List<Member>> createMembers(@RequestBody List<CreateMemberDto> dtos) {
        List<Member> created = memberService.createMembers(dtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
