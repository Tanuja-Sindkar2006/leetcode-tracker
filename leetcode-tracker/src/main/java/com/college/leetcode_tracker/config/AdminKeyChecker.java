package com.college.leetcode_tracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AdminKeyChecker {

    @Value("${app.admin-key}")
    private String adminKey;

    public void check(String providedKey) {
        if (providedKey == null || !providedKey.equals(adminKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid admin key");
        }
    }
}