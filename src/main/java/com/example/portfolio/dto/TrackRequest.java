package com.example.portfolio.dto;

import jakarta.validation.constraints.NotBlank;

public class TrackRequest {
    @NotBlank
    private String path; // e.g. "/" or "/projects"

    // optional
    private String referrer;

    // getters & setters
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getReferrer() { return referrer; }
    public void setReferrer(String referrer) { this.referrer = referrer; }
}
