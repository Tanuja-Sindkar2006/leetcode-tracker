package com.college.leetcode_tracker.dto;

import com.college.leetcode_tracker.entity.DailyStat;

import java.time.LocalDate;

public record StatPoint(LocalDate date, int total, int easy, int medium, int hard) {

    public static StatPoint from(DailyStat d) {
        return new StatPoint(d.getStatDate(), d.getTotalSolved(), d.getEasy(), d.getMedium(), d.getHard());
    }
}