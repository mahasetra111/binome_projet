package com.example.demo.controller;

import com.example.demo.dto.AssignCollectivityIdentityDto;
import com.example.demo.dto.CreateCollectivityDto;
import com.example.demo.model.Collectivity;
import com.example.demo.service.CollectivityIdentityService;
import com.example.demo.service.CollectivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
}