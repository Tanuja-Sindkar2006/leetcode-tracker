package com.college.leetcode_tracker.controller;


import com.college.leetcode_tracker.client.LeetCodeClient;
import com.college.leetcode_tracker.dto.LeetCodeStats;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final LeetCodeClient client;

    @GetMapping("/leetcode/{username}")
    public Object test(@PathVariable String username) {
        Optional<LeetCodeStats> stats = client.fetch(username);
        if (stats.isPresent()) {
            return stats.get();
        }
        return Map.of("message", "User not found");
    }
}