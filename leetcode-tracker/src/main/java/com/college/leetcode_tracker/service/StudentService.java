package com.college.leetcode_tracker.service;


import com.college.leetcode_tracker.client.LeetCodeClient;
import com.college.leetcode_tracker.dto.LeetCodeStats;
import com.college.leetcode_tracker.entity.Student;
import com.college.leetcode_tracker.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepo;
    private final LeetCodeClient leetCodeClient;
    private final StatsService statsService;

    public Student add(Student student) {
        String username = student.getLeetcodeUsername().trim();

        // 1. already registered?
        if (studentRepo.existsByLeetcodeUsernameIgnoreCase(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "This LeetCode username is already registered");
        }

        // 2. does it exist on LeetCode?
        LeetCodeStats stats = leetCodeClient.fetch(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "LeetCode user '" + username + "' not found"));

        // 3. save student, then save the first daily row
        student.setId(null);
        student.setLeetcodeUsername(username);
        Student saved = studentRepo.save(student);
        statsService.snapshot(saved, stats);
        return saved;
    }

    public List<Student> all() {
        return studentRepo.findAll();
    }

    public Student get(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }
}