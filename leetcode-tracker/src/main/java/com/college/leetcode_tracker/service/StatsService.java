package com.college.leetcode_tracker.service;

import com.college.leetcode_tracker.client.LeetCodeClient;
import com.college.leetcode_tracker.dto.LeetCodeStats;
import com.college.leetcode_tracker.entity.DailyStat;
import com.college.leetcode_tracker.entity.Student;
import com.college.leetcode_tracker.repository.DailyStatRepository;
import com.college.leetcode_tracker.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    private final DailyStatRepository statRepo;
    private final StudentRepository studentRepo;
    private final LeetCodeClient leetCodeClient;

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

    /** Fetch every student one by one, with a pause so we do not hammer LeetCode. */
    public void refreshAll() {
        List<Student> students = studentRepo.findAll();
        log.info("Refreshing LeetCode stats for {} students", students.size());

        int updated = 0;
        for (Student s : students) {
            try {
                Optional<LeetCodeStats> stats = leetCodeClient.fetch(s.getLeetcodeUsername());
                if (stats.isPresent()) {
                    snapshot(s, stats.get());
                    updated++;
                } else {
                    log.warn("LeetCode user not found: {}", s.getLeetcodeUsername());
                }
            } catch (Exception e) {
                // one student failing must not stop the others
                log.error("Failed for {}: {}", s.getLeetcodeUsername(), e.getMessage());
            }
            pause(1000);
        }
        log.info("Refresh finished: {}/{} updated", updated, students.size());
    }

    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}