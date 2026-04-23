package com.example.demo.controller;

import com.example.demo.dto.AssignCollectivityIdentityDto;
import com.example.demo.dto.CollectivityInformation;
import com.example.demo.dto.CreateCollectivityDto;
import com.example.demo.model.Collectivity;
import com.example.demo.service.CollectivityIdentityService;
import com.example.demo.service.CollectivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {

    private final CollectivityService collectivityService;
    private final CollectivityIdentityService collectivityIdentityService;

    public CollectivityController(CollectivityService collectivityService,
                                  CollectivityIdentityService collectivityIdentityService) {
        this.collectivityService = collectivityService;
        this.collectivityIdentityService = collectivityIdentityService;
    }

    @GetMapping
    public List<Collectivity> getAll() {
        return collectivityService.findAll();
    }

    @GetMapping("/{id}")
    public Collectivity getById(@PathVariable String id) {
        return collectivityService.getCollectivityWithMembers(id);
    }

    @GetMapping("/{id}/financialAccounts")
    public List<Map<String, Object>> getAccounts(
            @PathVariable String id,
            @RequestParam String at
    ) {
        return collectivityService.getAccountsAtDate(id, at);
    }

    @PostMapping
    public ResponseEntity<List<Collectivity>> createCollectivities(
            @RequestBody List<CreateCollectivityDto> dtos) {
        List<Collectivity> created = collectivityService.createCollectivities(dtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PatchMapping("/{id}/identity")
    public ResponseEntity<Collectivity> assignIdentity(
            @PathVariable UUID id,
            @RequestBody AssignCollectivityIdentityDto dto) {
        Collectivity updated = collectivityIdentityService.assignIdentity(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/informations")
    public ResponseEntity<?> updateCollectivity(
            @PathVariable String id,
            @RequestBody CollectivityInformation dto
    ) {
        Collectivity updated = collectivityService.update(id, dto);
        return ResponseEntity.ok(updated);
    }
}