package com.example.portfolio.dto;

import java.util.List;

public class StatsResponse {
    private long totalVisits;
    private long visitsToday;       
    private long uniqueVisitors;
    private List<String> lastVisits; // json strings: timestamp | ip | userAgent | path

    // Getters & setters
    public long getTotalVisits() { return totalVisits; }
    public void setTotalVisits(long totalVisits) { this.totalVisits = totalVisits; }

    public long getVisitsToday() { return visitsToday; }    
    public void setVisitsToday(long visitsToday) { this.visitsToday = visitsToday; }  
    public long getUniqueVisitors() { return uniqueVisitors; }
    public void setUniqueVisitors(long uniqueVisitors) { this.uniqueVisitors = uniqueVisitors; }

    public List<String> getLastVisits() { return lastVisits; }
    public void setLastVisits(List<String> lastVisits) { this.lastVisits = lastVisits; }
}
