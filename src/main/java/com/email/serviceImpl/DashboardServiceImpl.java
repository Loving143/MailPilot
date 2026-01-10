package com.email.serviceImpl;

import com.email.constants.EmailStatus;
import com.email.entity.Person;
import com.email.exception.BadRequestException;
import com.email.repository.EmailLogRepository;
import com.email.repository.PersonRepository;
import com.email.resposne.DashboardStatsResponse;
import com.email.resposne.DetailedStatsResponse;
import com.email.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);

    @Autowired
    private EmailLogRepository emailLogRepository;

    @Autowired
    private PersonRepository personRepository;

    public DashboardServiceImpl() {
        logger.info("DashboardServiceImpl initialized");
    }

    // Response statuses that indicate engagement
    private static final List<EmailStatus> RESPONSE_STATUSES = Arrays.asList(
            EmailStatus.EMAIL_RECEIVED,
            EmailStatus.CONTACTED_ON_PHONE,
            EmailStatus.Interview_Scheduled,
            EmailStatus.HIRED
    );

    @Override
    public DashboardStatsResponse getDashboardStats() {
        logger.info("Getting dashboard statistics");
        try {
            String currentUsername = getCurrentUsername();
            Person person = getPersonByEmail(currentUsername);
            Integer personId = person.getId();

            // Current period (today)
            LocalDateTime todayStart = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
            LocalDateTime todayEnd = todayStart.plusDays(1);

            // Previous period (yesterday)
            LocalDateTime yesterdayStart = todayStart.minusDays(1);
            LocalDateTime yesterdayEnd = todayStart;

            // Get current stats
            Long totalEmails = emailLogRepository.countTotalEmailsByPersonId(personId);
            Long sentToday = emailLogRepository.countEmailsByPersonIdAndDateRange(personId, todayStart, todayEnd);
            Long totalResponses = emailLogRepository.countResponsesByPersonId(personId, RESPONSE_STATUSES);
            Long activeCampaigns = emailLogRepository.countActiveCampaignDaysByPersonId(personId, todayStart.minusDays(30));

            // Get previous stats for comparison
            Long sentYesterday = emailLogRepository.countEmailsByPersonIdAndDateRange(personId, yesterdayStart, yesterdayEnd);
            Long totalEmailsLastWeek = emailLogRepository.countEmailsByPersonIdAndPreviousPeriod(
                    personId, todayStart.minusDays(7), todayStart);

            // Calculate response rate
            Double responseRate = totalEmails > 0 ? (totalResponses.doubleValue() / totalEmails.doubleValue()) * 100 : 0.0;

            // Calculate growth percentages
            String totalEmailsGrowth = calculateGrowthPercentage(totalEmails, totalEmailsLastWeek);
            String sentTodayGrowth = calculateGrowthPercentage(sentToday, sentYesterday);
            String responseRateGrowth = "+15%"; // Placeholder - would need historical data
            String activeCampaignsGrowth = "+5%"; // Placeholder - would need historical data

            DashboardStatsResponse response = new DashboardStatsResponse(
                    totalEmails,
                    totalEmailsGrowth,
                    sentToday,
                    sentTodayGrowth,
                    responseRate,
                    responseRateGrowth,
                    activeCampaigns,
                    activeCampaignsGrowth
            );

            logger.info("Dashboard statistics retrieved successfully for user: {}", currentUsername);
            return response;

        } catch (Exception e) {
            logger.error("Error getting dashboard statistics", e);
            throw e;
        }
    }

    @Override
    public DetailedStatsResponse getDetailedStats() {
        logger.info("Getting detailed statistics");
        try {
            String currentUsername = getCurrentUsername();
            Person person = getPersonByEmail(currentUsername);
            Integer personId = person.getId();

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime todayStart = now.truncatedTo(ChronoUnit.DAYS);
            LocalDateTime weekStart = todayStart.minusDays(7);
            LocalDateTime monthStart = todayStart.minusDays(30);

            // Get counts
            Long totalEmails = emailLogRepository.countTotalEmailsByPersonId(personId);
            Long sentToday = emailLogRepository.countEmailsByPersonIdAndDateRange(personId, todayStart, todayStart.plusDays(1));
            Long sentThisWeek = emailLogRepository.countEmailsByPersonIdAndDateRange(personId, weekStart, todayStart.plusDays(1));
            Long sentThisMonth = emailLogRepository.countEmailsByPersonIdAndDateRange(personId, monthStart, todayStart.plusDays(1));
            Long totalResponses = emailLogRepository.countResponsesByPersonId(personId, RESPONSE_STATUSES);
            Long uniqueCompanies = emailLogRepository.countUniqueCompaniesByPersonId(personId);
            Long activeCampaigns = emailLogRepository.countActiveCampaignDaysByPersonId(personId, monthStart);

            // Calculate response rate
            Double responseRate = totalEmails > 0 ? (totalResponses.doubleValue() / totalEmails.doubleValue()) * 100 : 0.0;

            // Get status breakdown
            Map<String, Long> statusBreakdown = getStatusBreakdown(personId);

            DetailedStatsResponse response = new DetailedStatsResponse();
            response.setTotalEmails(totalEmails);
            response.setSentToday(sentToday);
            response.setSentThisWeek(sentThisWeek);
            response.setSentThisMonth(sentThisMonth);
            response.setResponseRate(responseRate);
            response.setTotalResponses(totalResponses);
            response.setStatusBreakdown(statusBreakdown);
            response.setActiveCampaigns(activeCampaigns);
            response.setUniqueCompanies(uniqueCompanies);
            response.setAverageResponseTime("2.5 days"); // Placeholder - would need more complex calculation

            logger.info("Detailed statistics retrieved successfully for user: {}", currentUsername);
            return response;

        } catch (Exception e) {
            logger.error("Error getting detailed statistics", e);
            throw e;
        }
    }

    @Override
    public DashboardStatsResponse getDashboardStatsByDateRange(String startDate, String endDate) {
        logger.info("Getting dashboard statistics for date range: {} to {}", startDate, endDate);
        try {
            String currentUsername = getCurrentUsername();
            Person person = getPersonByEmail(currentUsername);
            Integer personId = person.getId();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
            LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");

            Long totalEmails = emailLogRepository.countEmailsByPersonIdAndDateRange(personId, start, end);
            Long totalResponses = emailLogRepository.countResponsesByPersonId(personId, RESPONSE_STATUSES);
            Double responseRate = totalEmails > 0 ? (totalResponses.doubleValue() / totalEmails.doubleValue()) * 100 : 0.0;

            DashboardStatsResponse response = new DashboardStatsResponse(
                    totalEmails,
                    "0%", // No comparison for custom date range
                    totalEmails, // Using total as "sent today" for custom range
                    "0%",
                    responseRate,
                    "0%",
                    1L, // Placeholder
                    "0%"
            );

            logger.info("Dashboard statistics for date range retrieved successfully");
            return response;

        } catch (Exception e) {
            logger.error("Error getting dashboard statistics for date range", e);
            throw e;
        }
    }

    @Override
    public DetailedStatsResponse getStatsByPeriod(String period) {
        logger.info("Getting statistics for period: {}", period);
        try {
            String currentUsername = getCurrentUsername();
            Person person = getPersonByEmail(currentUsername);
            Integer personId = person.getId();

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDate;

            switch (period.toLowerCase()) {
                case "today":
                    startDate = now.truncatedTo(ChronoUnit.DAYS);
                    break;
                case "week":
                    startDate = now.minusDays(7);
                    break;
                case "month":
                    startDate = now.minusDays(30);
                    break;
                case "year":
                    startDate = now.minusDays(365);
                    break;
                default:
                    throw new BadRequestException("Invalid period. Use: today, week, month, or year");
            }

            Long totalEmails = emailLogRepository.countEmailsByPersonIdAndDateRange(personId, startDate, now);
            Long totalResponses = emailLogRepository.countResponsesByPersonId(personId, RESPONSE_STATUSES);
            Double responseRate = totalEmails > 0 ? (totalResponses.doubleValue() / totalEmails.doubleValue()) * 100 : 0.0;

            Map<String, Long> statusBreakdown = getStatusBreakdown(personId);

            DetailedStatsResponse response = new DetailedStatsResponse();
            response.setTotalEmails(totalEmails);
            response.setSentToday(totalEmails);
            response.setSentThisWeek(totalEmails);
            response.setSentThisMonth(totalEmails);
            response.setResponseRate(responseRate);
            response.setTotalResponses(totalResponses);
            response.setStatusBreakdown(statusBreakdown);
            response.setActiveCampaigns(1L);
            response.setUniqueCompanies(emailLogRepository.countUniqueCompaniesByPersonId(personId));
            response.setAverageResponseTime("2.5 days");

            logger.info("Statistics for period {} retrieved successfully", period);
            return response;

        } catch (Exception e) {
            logger.error("Error getting statistics for period: {}", period, e);
            throw e;
        }
    }

    private String getCurrentUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private Person getPersonByEmail(String email) {
        return personRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    private String calculateGrowthPercentage(Long current, Long previous) {
        if (previous == null || previous == 0) {
            return current > 0 ? "+100%" : "0%";
        }
        
        double growth = ((current.doubleValue() - previous.doubleValue()) / previous.doubleValue()) * 100;
        return String.format("%+.0f%%", growth);
    }

    private Map<String, Long> getStatusBreakdown(Integer personId) {
        List<Object[]> results = emailLogRepository.getStatusBreakdownByPersonId(personId);
        Map<String, Long> breakdown = new HashMap<>();
        
        for (Object[] result : results) {
            EmailStatus status = (EmailStatus) result[0];
            Long count = (Long) result[1];
            breakdown.put(status.getValue(), count);
        }
        
        return breakdown;
    }
}