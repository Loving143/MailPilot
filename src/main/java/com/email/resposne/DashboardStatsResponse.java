package com.email.resposne;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DashboardStatsResponse {
    
    @JsonProperty("totalEmails")
    private Long totalEmails;
    
    @JsonProperty("totalEmailsGrowth")
    private String totalEmailsGrowth;
    
    @JsonProperty("sentToday")
    private Long sentToday;
    
    @JsonProperty("sentTodayGrowth")
    private String sentTodayGrowth;
    
    @JsonProperty("responseRate")
    private Double responseRate;
    
    @JsonProperty("responseRateGrowth")
    private String responseRateGrowth;
    
    @JsonProperty("activeCampaigns")
    private Long activeCampaigns;
    
    @JsonProperty("activeCampaignsGrowth")
    private String activeCampaignsGrowth;

    // Constructors
    public DashboardStatsResponse() {}

    public DashboardStatsResponse(Long totalEmails, String totalEmailsGrowth, 
                                Long sentToday, String sentTodayGrowth,
                                Double responseRate, String responseRateGrowth,
                                Long activeCampaigns, String activeCampaignsGrowth) {
        this.totalEmails = totalEmails;
        this.totalEmailsGrowth = totalEmailsGrowth;
        this.sentToday = sentToday;
        this.sentTodayGrowth = sentTodayGrowth;
        this.responseRate = responseRate;
        this.responseRateGrowth = responseRateGrowth;
        this.activeCampaigns = activeCampaigns;
        this.activeCampaignsGrowth = activeCampaignsGrowth;
    }

    // Getters and Setters
    public Long getTotalEmails() {
        return totalEmails;
    }

    public void setTotalEmails(Long totalEmails) {
        this.totalEmails = totalEmails;
    }

    public String getTotalEmailsGrowth() {
        return totalEmailsGrowth;
    }

    public void setTotalEmailsGrowth(String totalEmailsGrowth) {
        this.totalEmailsGrowth = totalEmailsGrowth;
    }

    public Long getSentToday() {
        return sentToday;
    }

    public void setSentToday(Long sentToday) {
        this.sentToday = sentToday;
    }

    public String getSentTodayGrowth() {
        return sentTodayGrowth;
    }

    public void setSentTodayGrowth(String sentTodayGrowth) {
        this.sentTodayGrowth = sentTodayGrowth;
    }

    public Double getResponseRate() {
        return responseRate;
    }

    public void setResponseRate(Double responseRate) {
        this.responseRate = responseRate;
    }

    public String getResponseRateGrowth() {
        return responseRateGrowth;
    }

    public void setResponseRateGrowth(String responseRateGrowth) {
        this.responseRateGrowth = responseRateGrowth;
    }

    public Long getActiveCampaigns() {
        return activeCampaigns;
    }

    public void setActiveCampaigns(Long activeCampaigns) {
        this.activeCampaigns = activeCampaigns;
    }

    public String getActiveCampaignsGrowth() {
        return activeCampaignsGrowth;
    }

    public void setActiveCampaignsGrowth(String activeCampaignsGrowth) {
        this.activeCampaignsGrowth = activeCampaignsGrowth;
    }

    @Override
    public String toString() {
        return "DashboardStatsResponse{" +
                "totalEmails=" + totalEmails +
                ", totalEmailsGrowth='" + totalEmailsGrowth + '\'' +
                ", sentToday=" + sentToday +
                ", sentTodayGrowth='" + sentTodayGrowth + '\'' +
                ", responseRate=" + responseRate +
                ", responseRateGrowth='" + responseRateGrowth + '\'' +
                ", activeCampaigns=" + activeCampaigns +
                ", activeCampaignsGrowth='" + activeCampaignsGrowth + '\'' +
                '}';
    }
}