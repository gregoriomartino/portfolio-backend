package com.example.portfolio.controller;

import com.example.portfolio.dto.StatsResponse;
import com.example.portfolio.dto.TrackRequest;
import com.example.portfolio.service.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {
    "http://localhost:3000",
    "https://gregoriomartino.github.io",
    "https://gregoriomartino.github.io/gregorio-martino-portfolio",
    "https://portfolio-backend-mx08.onrender.com"
})


public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping("/track")
    public ResponseEntity<?> track(@Valid @RequestBody TrackRequest body, HttpServletRequest request) {
        String ip = extractClientIp(request);
        String ua = request.getHeader("User-Agent");
        analyticsService.trackVisit(ip, ua, body.getPath(), body.getReferrer());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> stats() {
        StatsResponse s = new StatsResponse();
        s.setTotalVisits(analyticsService.getTotalVisits());
        s.setVisitsToday(analyticsService.getVisitsToday());
        s.setUniqueVisitors(analyticsService.getUniqueVisitors());
        List<String> last = analyticsService.getLastVisits();
        s.setLastVisits(last);
        return ResponseEntity.ok(s);
    }

    private String extractClientIp(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        if (xf == null) {
            return request.getRemoteAddr();
        }
        return xf.split(",")[0].trim();
    }
}
