package com.college.leetcode_tracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Branch is required")
    private String branch;

    @NotNull(message = "Year is required")
    @Min(1) @Max(4)
    private Integer year;

    @NotBlank(message = "LeetCode username is required")
    @Column(unique = true)
    private String leetcodeUsername;

    private LocalDateTime createdAt = LocalDateTime.now();
}
