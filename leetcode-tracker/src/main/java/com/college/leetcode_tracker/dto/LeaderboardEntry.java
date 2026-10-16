package com.college.leetcode_tracker.dto;

public record LeaderboardEntry(
        int rank,
        Long studentId,
        String name,
        String branch,
        Integer year,
        String leetcodeUsername,
        int totalSolved,
        int easy,
        int medium,
        int hard,
        int gain) {}