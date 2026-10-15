package com.college.leetcode_tracker.controller;

import com.college.leetcode_tracker.config.AdminKeyChecker;
import com.college.leetcode_tracker.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final StatsService statsService;
    private final AdminKeyChecker adminKeyChecker;

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(
            @RequestHeader(value = "X-Admin-Key", required = false) String key) {

        adminKeyChecker.check(key);
        CompletableFuture.runAsync(statsService::refreshAll);   // runs in the background
        return ResponseEntity.accepted()
                .body(Map.of("message", "Refresh started. Check the backend console."));
    }
}