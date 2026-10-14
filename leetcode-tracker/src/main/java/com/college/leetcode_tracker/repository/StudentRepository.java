package com.college.leetcode_tracker.repository;

import com.college.leetcode_tracker.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByLeetcodeUsernameIgnoreCase(String leetcodeUsername);
}