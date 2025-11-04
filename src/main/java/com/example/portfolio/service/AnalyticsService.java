package com.example.portfolio.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AnalyticsService {

    private final StringRedisTemplate redis;
    private static final String TOTAL_KEY = "visits:total";
    private static final String UNIQUE_SET = "visitors:unique";
    private static final String LOG_LIST = "visits:log";
    private static final int LOG_SIZE = 20;

    public AnalyticsService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void trackVisit(String ip, String userAgent, String path, String referrer) {
        // increment total
        redis.opsForValue().increment(TOTAL_KEY);

        // increment daily count
        String todayKey = getTodayKey();
        redis.opsForValue().increment(todayKey);

        // add to unique set (by IP)
        if (ip != null) {
            redis.opsForSet().add(UNIQUE_SET, ip);
        }

        // push log entry (timestamp | ip | userAgent | path)
        String ts = DateTimeFormatter.ISO_INSTANT.format(Instant.now().atOffset(ZoneOffset.UTC));
        String safeUa = userAgent == null ? "-" : userAgent.replaceAll("\\n"," ").replaceAll("\\r"," ");
        String entry = String.format("%s | %s | %s | %s", ts, ip == null ? "-" : ip, safeUa, path == null ? "-" : path);

        redis.opsForList().leftPush(LOG_LIST, entry);
        redis.opsForList().trim(LOG_LIST, 0, LOG_SIZE - 1); // keep only last LOG_SIZE
    }

    public long getTotalVisits() {
        String v = redis.opsForValue().get(TOTAL_KEY);
        return v == null ? 0 : Long.parseLong(v);
    }

    public long getUniqueVisitors() {
        Long s = redis.opsForSet().size(UNIQUE_SET);
        return s == null ? 0 : s;
    }

    public long getVisitsToday() {
        String v = redis.opsForValue().get(getTodayKey());
        return v == null ? 0 : Long.parseLong(v);
    }

    public List<String> getLastVisits() {
        return redis.opsForList().range(LOG_LIST, 0, LOG_SIZE - 1);
    }

    private String getTodayKey() {
        String today = DateTimeFormatter.ofPattern("yyyyMMdd")
                .format(Instant.now().atZone(ZoneOffset.UTC));
        return "visits:" + today;
    }
}
