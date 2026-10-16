package com.college.leetcode_tracker.controller;

import com.college.leetcode_tracker.dto.LeaderboardEntry;
import com.college.leetcode_tracker.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping
    public List<LeaderboardEntry> get(
            @RequestParam(defaultValue = "total") String type,
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) Integer year) {

        return leaderboardService.build(type, branch, year);
    }
}