package com.example.advancedJavaProgramming.service;

import com.example.advancedJavaProgramming.repository.ContestRepository;
import com.example.advancedJavaProgramming.model.Contest;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContestService {

    private ContestRepository contestRepository;

    // 전체 공모전
    public List<Map<String, Object>> getAllContests() {
        List<Contest> contests = contestRepository.findAll();
        return enrichContestData(contests);
    }

    // 모든 키워드 (중복 제거)
    public Set<String> getAllKeywords() {
        List<Contest> contests = contestRepository.findAll();
        return contests.stream()
                .map(c -> c.getInfo().get("분야"))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    // 필터별 공모전
    public List<Map<String, Object>> filterContests(String filter, String keyword) {
        List<Contest> contests;
        if ("popular".equals(filter)) {
            contests = contestRepository.findAll(Sort.by(Sort.Direction.DESC, "read_count"));
        } else if ("recent".equals(filter)) {
            contests = contestRepository.findAll(Sort.by(Sort.Direction.DESC, "_id"));
        } else if ("keyword".equals(filter) && keyword != null && !keyword.isEmpty()) {
            contests = contestRepository.findAll().stream()
                    .filter(c -> c.getInfo().get("분야") != null &&
                            c.getInfo().get("분야").toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        } else {
            contests = contestRepository.findAll();
        }
        return enrichContestData(contests);
    }

    // 🔥 공통 처리: D-Day, prize, organizer 추가
    private List<Map<String, Object>> enrichContestData(List<Contest> contests) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Contest contest : contests) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", contest.getId());
            map.put("title", contest.getTitle());
            map.put("img_src", contest.getImgSrc());
            map.put("read_count", contest.getReadCount());
            map.put("info", contest.getInfo());
            map.put("dday", calculateDday(contest.getInfo().get("접수마감일")));
            map.put("prize", contest.getInfo().get("총 상금"));
            map.put("organizer", contest.getInfo().get("주최/주관"));
            result.add(map);
        }
        return result;
    }

    // 🔥 D-Day 계산 함수 (예시)
    private long calculateDday(String endDateStr) {
        try {
            java.time.LocalDate endDate = java.time.LocalDate.parse(endDateStr);
            java.time.LocalDate now = java.time.LocalDate.now();
            return java.time.temporal.ChronoUnit.DAYS.between(now, endDate);
        } catch (Exception e) {
            return -1; // 날짜가 없거나 형식이 맞지 않으면 -1
        }
    }
}
