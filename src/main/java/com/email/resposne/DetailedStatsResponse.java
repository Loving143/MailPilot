package com.email.resposne;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class DetailedStatsResponse {
    
    @JsonProperty("totalEmails")
    private Long totalEmails;
    
    @JsonProperty("sentToday")
    private Long sentToday;
    
    @JsonProperty("sentThisWeek")
    private Long sentThisWeek;
    
    @JsonProperty("sentThisMonth")
    private Long sentThisMonth;
    
    @JsonProperty("responseRate")
    private Double responseRate;
    
    @JsonProperty("totalResponses")
    private Long totalResponses;
    
    @JsonProperty("statusBreakdown")
    private Map<String, Long> statusBreakdown;
    
    @JsonProperty("activeCampaigns")
    private Long activeCampaigns;
    
    @JsonProperty("uniqueCompanies")
    private Long uniqueCompanies;
    
    @JsonProperty("averageResponseTime")
    private String averageResponseTime;

    // Constructors
    public DetailedStatsResponse() {}

    // Getters and Setters
    public Long getTotalEmails() {
        return totalEmails;
    }

    public void setTotalEmails(Long totalEmails) {
        this.totalEmails = totalEmails;
    }

    public Long getSentToday() {
        return sentToday;
    }

    public void setSentToday(Long sentToday) {
        this.sentToday = sentToday;
    }

    public Long getSentThisWeek() {
        return sentThisWeek;
    }

    public void setSentThisWeek(Long sentThisWeek) {
        this.sentThisWeek = sentThisWeek;
    }

    public Long getSentThisMonth() {
        return sentThisMonth;
    }

    public void setSentThisMonth(Long sentThisMonth) {
        this.sentThisMonth = sentThisMonth;
    }

    public Double getResponseRate() {
        return responseRate;
    }

    public void setResponseRate(Double responseRate) {
        this.responseRate = responseRate;
    }

    public Long getTotalResponses() {
        return totalResponses;
    }

    public void setTotalResponses(Long totalResponses) {
        this.totalResponses = totalResponses;
    }

    public Map<String, Long> getStatusBreakdown() {
        return statusBreakdown;
    }

    public void setStatusBreakdown(Map<String, Long> statusBreakdown) {
        this.statusBreakdown = statusBreakdown;
    }

    public Long getActiveCampaigns() {
        return activeCampaigns;
    }

    public void setActiveCampaigns(Long activeCampaigns) {
        this.activeCampaigns = activeCampaigns;
    }

    public Long getUniqueCompanies() {
        return uniqueCompanies;
    }

    public void setUniqueCompanies(Long uniqueCompanies) {
        this.uniqueCompanies = uniqueCompanies;
    }

    public String getAverageResponseTime() {
        return averageResponseTime;
    }

    public void setAverageResponseTime(String averageResponseTime) {
        this.averageResponseTime = averageResponseTime;
    }

    @Override
    public String toString() {
        return "DetailedStatsResponse{" +
                "totalEmails=" + totalEmails +
                ", sentToday=" + sentToday +
                ", sentThisWeek=" + sentThisWeek +
                ", sentThisMonth=" + sentThisMonth +
                ", responseRate=" + responseRate +
                ", totalResponses=" + totalResponses +
                ", statusBreakdown=" + statusBreakdown +
                ", activeCampaigns=" + activeCampaigns +
                ", uniqueCompanies=" + uniqueCompanies +
                ", averageResponseTime='" + averageResponseTime + '\'' +
                '}';
    }
}