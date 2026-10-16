package com.college.leetcode_tracker.service;

import com.college.leetcode_tracker.dto.LeaderboardEntry;
import com.college.leetcode_tracker.entity.DailyStat;
import com.college.leetcode_tracker.entity.Student;
import com.college.leetcode_tracker.repository.DailyStatRepository;
import com.college.leetcode_tracker.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    private final StudentRepository studentRepo;
    private final DailyStatRepository statRepo;

    // a small helper holder used only inside this class
    private record Row(Student student, DailyStat latest, int gain) {}

    public List<LeaderboardEntry> build(String type, String branch, Integer year) {

        // 1. how many days back should we compare?
        int days = switch (type) {
            case "total" -> 0;
            case "today" -> 1;
            case "weekly" -> 7;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "type must be total, weekly or today");
        };
        LocalDate cutoff = LocalDate.now(IST).minusDays(days);

        // 2. build one row per student
        List<Row> rows = new ArrayList<>();
        for (Student s : studentRepo.findAll()) {

            // filters
            if (branch != null && !branch.isBlank() && !s.getBranch().equalsIgnoreCase(branch)) continue;
            if (year != null && !year.equals(s.getYear())) continue;

            DailyStat latest = statRepo.findTopByStudentIdOrderByStatDateDesc(s.getId()).orElse(null);
            if (latest == null) continue;   // no data yet

            int gain = 0;
            if (days > 0) {
                DailyStat base = statRepo
                        .findTopByStudentIdAndStatDateLessThanEqualOrderByStatDateDesc(s.getId(), cutoff)
                        .or(() -> statRepo.findTopByStudentIdOrderByStatDateAsc(s.getId()))
                        .orElse(latest);
                gain = latest.getTotalSolved() - base.getTotalSolved();
            }
            rows.add(new Row(s, latest, gain));
        }

        // 3. sort: by gain (for today/weekly) then by total; for "total" only by total
        Comparator<Row> byTotal = Comparator.comparingInt((Row r) -> r.latest().getTotalSolved()).reversed();
        Comparator<Row> order = days > 0
                ? Comparator.comparingInt(Row::gain).reversed().thenComparing(byTotal)
                : byTotal;
        rows.sort(order);

        // 4. add rank numbers and convert to the response objects
        List<LeaderboardEntry> result = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            Row r = rows.get(i);
            Student s = r.student();
            DailyStat d = r.latest();
            result.add(new LeaderboardEntry(i + 1, s.getId(), s.getName(), s.getBranch(), s.getYear(),
                    s.getLeetcodeUsername(), d.getTotalSolved(), d.getEasy(), d.getMedium(), d.getHard(),
                    r.gain()));
        }
        return result;
    }
}