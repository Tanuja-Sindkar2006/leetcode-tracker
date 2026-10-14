package com.college.leetcode_tracker.client;


import com.college.leetcode_tracker.dto.LeetCodeStats;
import com.college.leetcode_tracker.exception.LeetCodeException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class LeetCodeClient {

    private static final String QUERY =
            "query userProblemsSolved($username: String!) { "
                    + "matchedUser(username: $username) { "
                    + "submitStatsGlobal { acSubmissionNum { difficulty count } } } }";

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://leetcode.com")
            .defaultHeader("User-Agent", "Mozilla/5.0")
            .defaultHeader("Referer", "https://leetcode.com")
            .build();

    /** Empty = username not found. Throws LeetCodeException = LeetCode unreachable. */
    @SuppressWarnings("unchecked")
    public Optional<LeetCodeStats> fetch(String username) {
        Map<String, Object> body = Map.of(
                "query", QUERY,
                "variables", Map.of("username", username));
        try {
            Map<String, Object> root = restClient.post()
                    .uri("/graphql")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {});

            if (root == null) return Optional.empty();
            Map<String, Object> data = (Map<String, Object>) root.get("data");
            if (data == null) return Optional.empty();
            Map<String, Object> user = (Map<String, Object>) data.get("matchedUser");
            if (user == null) return Optional.empty();          // username does not exist

            Map<String, Object> stats = (Map<String, Object>) user.get("submitStatsGlobal");
            List<Map<String, Object>> list = (List<Map<String, Object>>) stats.get("acSubmissionNum");

            int total = 0, easy = 0, medium = 0, hard = 0;
            for (Map<String, Object> item : list) {
                String difficulty = String.valueOf(item.get("difficulty"));
                int count = ((Number) item.get("count")).intValue();
                switch (difficulty) {
                    case "All" -> total = count;
                    case "Easy" -> easy = count;
                    case "Medium" -> medium = count;
                    case "Hard" -> hard = count;
                    default -> { }
                }
            }
            return Optional.of(new LeetCodeStats(total, easy, medium, hard));

        } catch (RestClientException e) {
            throw new LeetCodeException("Could not reach LeetCode. Try again later.", e);
        }
    }
}