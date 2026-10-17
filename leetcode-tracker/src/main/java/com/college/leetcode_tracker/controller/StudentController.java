package com.college.leetcode_tracker.controller;

import com.college.leetcode_tracker.dto.StatPoint;
import com.college.leetcode_tracker.entity.Student;
import com.college.leetcode_tracker.service.StatsService;
import com.college.leetcode_tracker.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final StatsService statsService;

    @PostMapping
    public ResponseEntity<Student> add(@Valid @RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.add(student));
    }

    @GetMapping
    public List<Student> all() {
        return studentService.all();
    }

    @GetMapping("/{id}")
    public Student get(@PathVariable Long id) {
        return studentService.get(id);
    }

    @GetMapping("/{id}/stats")
    public List<StatPoint> stats(@PathVariable Long id) {
        return statsService.history(id);
    }
}