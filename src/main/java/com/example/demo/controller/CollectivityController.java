package com.example.demo.controller;


import com.example.demo.dto.CreateCollectivityDto;
import com.example.demo.model.Collectivity;
import com.example.demo.service.CollectivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {

    private final CollectivityService collectivityService;

    public CollectivityController(CollectivityService collectivityService) {
        this.collectivityService = collectivityService;
    }

    @PostMapping
    public ResponseEntity<List<Collectivity>> createCollectivities(
            @RequestBody List<CreateCollectivityDto> dtos) {
        List<Collectivity> created = collectivityService.createCollectivities(dtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @GetMapping
    public ResponseEntity<List<Collectivity>> getCollectivities() {
        List<Collectivity> collectivities = collectivityService.getAllCollectivities();
        return ResponseEntity.ok(collectivities);
    }
    @PutMapping("/collectivities/{id}/informations")
    public ResponseEntity<Collectivity> updateCollectivity(
            @PathVariable String id,
            @RequestBody CollectivityInformation dto) {
        return ResponseEntity.ok(collectivityService.update(id, dto));
    }
}