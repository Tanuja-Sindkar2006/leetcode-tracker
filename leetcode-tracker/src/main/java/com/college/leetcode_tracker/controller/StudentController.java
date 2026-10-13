package com.college.leetcode_tracker.controller;


import com.college.leetcode_tracker.entity.Student;
import com.college.leetcode_tracker.repository.StudentRepository;
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

    private final StudentRepository studentRepository;

    @PostMapping
    public ResponseEntity<Student> add(@Valid @RequestBody Student student) {
        student.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(studentRepository.save(student));
    }

    @GetMapping
    public List<Student> all() {
        return studentRepository.findAll();
    }
}
