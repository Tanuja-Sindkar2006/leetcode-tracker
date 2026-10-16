package com.college.leetcode_tracker.repository;

import com.college.leetcode_tracker.entity.DailyStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyStatRepository extends JpaRepository<DailyStat, Long> {

    Optional<DailyStat> findByStudentIdAndStatDate(Long studentId, LocalDate statDate);

    // newest snapshot of a student
    Optional<DailyStat> findTopByStudentIdOrderByStatDateDesc(Long studentId);

    // oldest snapshot of a student
    Optional<DailyStat> findTopByStudentIdOrderByStatDateAsc(Long studentId);

    // last snapshot on or before a given date
    Optional<DailyStat> findTopByStudentIdAndStatDateLessThanEqualOrderByStatDateDesc(Long studentId, LocalDate statDate);
}