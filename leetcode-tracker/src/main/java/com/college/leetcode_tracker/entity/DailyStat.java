package com.college.leetcode_tracker.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "daily_stats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "stat_date"}))
@Getter
@Setter
@NoArgsConstructor
public class DailyStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate;

    private int totalSolved;
    private int easy;
    private int medium;
    private int hard;
}