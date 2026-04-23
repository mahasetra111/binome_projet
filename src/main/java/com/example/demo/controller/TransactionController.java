package com.example.demo.controller;

import com.example.demo.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/collectivities")
public class TransactionController {

    private final TransactionService service = new TransactionService();

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Map<String, Object>>> getTransactions(
            @PathVariable String id,
            @RequestParam String from,
            @RequestParam String to
    ) {

        List<Map<String, Object>> result =
                service.getTransactions(id, from, to);

        return ResponseEntity.ok(result);
    }
}