package com.college.leetcode_tracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyStatsJob {

    private final StatsService statsService;

    // every day at 2:00 AM India time
    @Scheduled(cron = "0 0 2 * * *", zone = "Asia/Kolkata")
    public void run() {
        statsService.refreshAll();
    }
}