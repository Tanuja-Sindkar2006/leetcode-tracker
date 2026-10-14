package com.college.leetcode_tracker.service;


import com.college.leetcode_tracker.dto.LeetCodeStats;
import com.college.leetcode_tracker.entity.DailyStat;
import com.college.leetcode_tracker.entity.Student;
import com.college.leetcode_tracker.repository.DailyStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class StatsService {

    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    private final DailyStatRepository statRepo;

    /** Save today's numbers. If today's row already exists, update it. */
    public DailyStat snapshot(Student student, LeetCodeStats stats) {
        LocalDate today = LocalDate.now(IST);
        DailyStat row = statRepo.findByStudentIdAndStatDate(student.getId(), today)
                .orElseGet(DailyStat::new);
        row.setStudent(student);
        row.setStatDate(today);
        row.setTotalSolved(stats.total());
        row.setEasy(stats.easy());
        row.setMedium(stats.medium());
        row.setHard(stats.hard());
        return statRepo.save(row);
    }
}