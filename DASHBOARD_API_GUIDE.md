# Dashboard Statistics API Guide

## Overview

The Dashboard API provides comprehensive statistics about email campaigns, response rates, and performance metrics. All data is calculated from the `email_log` table and filtered by the authenticated user.

## Available Statistics

### Main Dashboard Metrics
- **Total Emails**: All-time email count for the user
- **Sent Today**: Emails sent in the current day
- **Response Rate**: Percentage of emails that received responses
- **Active Campaigns**: Number of active campaign days in the last 30 days

### Detailed Metrics
- **Time-based Counts**: Today, this week, this month
- **Status Breakdown**: Count by email status (Sent, Received, Interview Scheduled, etc.)
- **Unique Companies**: Number of different companies contacted
- **Growth Percentages**: Comparison with previous periods

## API Endpoints

### 1. Dashboard Statistics
**GET** `/api/dashboard/stats`

Returns the main dashboard statistics with growth percentages.

**Response:**
```json
{
  "status": "1",
  "message": "Dashboard stats retrieved successfully",
  "data": {
    "totalEmails": 150,
    "totalEmailsGrowth": "+12%",
    "sentToday": 5,
    "sentTodayGrowth": "+8%",
    "responseRate": 23.5,
    "responseRateGrowth": "+15%",
    "activeCampaigns": 3,
    "activeCampaignsGrowth": "+5%"
  }
}
```

### 2. Detailed Statistics
**GET** `/api/dashboard/stats/detailed`

Returns comprehensive statistics with breakdowns.

**Response:**
```json
{
  "status": "1",
  "message": "Detailed stats retrieved successfully",
  "data": {
    "totalEmails": 150,
    "sentToday": 5,
    "sentThisWeek": 25,
    "sentThisMonth": 80,
    "responseRate": 23.5,
    "totalResponses": 35,
    "statusBreakdown": {
      "Email Sent": 100,
      "Email Received": 30,
      "Interview_scheduled": 15,
      "Hired": 5
    },
    "activeCampaigns": 3,
    "uniqueCompanies": 45,
    "averageResponseTime": "2.5 days"
  }
}
```

### 3. Statistics by Date Range
**GET** `/api/dashboard/stats/range?startDate=2026-01-01&endDate=2026-01-31`

Returns statistics for a specific date range.

**Parameters:**
- `startDate`: Start date in YYYY-MM-DD format
- `endDate`: End date in YYYY-MM-DD format

### 4. Statistics by Period
**GET** `/api/dashboard/stats/period/{period}`

Returns detailed statistics for a specific period.

**Available Periods:**
- `today`: Current day
- `week`: Last 7 days
- `month`: Last 30 days
- `year`: Last 365 days

### 5. Dashboard Summary
**GET** `/api/dashboard/summary`

Returns both dashboard and detailed stats in one response.

**Response:**
```json
{
  "status": "1",
  "message": "Dashboard summary retrieved successfully",
  "data": {
    "dashboard": { /* Dashboard stats object */ },
    "detailed": { /* Detailed stats object */ }
  }
}
```

### 6. Quick Statistics
**GET** `/api/dashboard/quick-stats`

Returns simplified stats optimized for quick display or mobile widgets.

**Response:**
```json
{
  "status": "1",
  "message": "Quick stats retrieved successfully",
  "data": {
    "totalEmails": 150,
    "sentToday": 5,
    "responseRate": "23.5%",
    "activeCampaigns": 3
  }
}
```

## Frontend Implementation Examples

### JavaScript/Fetch API
```javascript
async function getDashboardStats() {
    try {
        const response = await fetch('/api/dashboard/stats', {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        
        const result = await response.json();
        
        if (result.status === '1') {
            const stats = result.data;
            // Update UI with stats
            updateDashboard(stats);
        }
    } catch (error) {
        console.error('Error fetching dashboard stats:', error);
    }
}
```

### React Hook Example
```jsx
import { useState, useEffect } from 'react';

const useDashboardStats = () => {
    const [stats, setStats] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchStats = async () => {
            try {
                const token = localStorage.getItem('authToken');
                const response = await fetch('/api/dashboard/stats', {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                const result = await response.json();
                
                if (result.status === '1') {
                    setStats(result.data);
                } else {
                    setError(result.message);
                }
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchStats();
    }, []);

    return { stats, loading, error };
};

// Usage in component
const Dashboard = () => {
    const { stats, loading, error } = useDashboardStats();

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div className="dashboard">
            <div className="stat-card">
                <h3>Total Emails</h3>
                <div className="stat-value">{stats.totalEmails}</div>
                <div className="stat-growth">{stats.totalEmailsGrowth}</div>
            </div>
            {/* More stat cards */}
        </div>
    );
};
```

### Vue.js Composition API Example
```vue
<template>
  <div class="dashboard">
    <div v-if="loading">Loading...</div>
    <div v-else-if="error">Error: {{ error }}</div>
    <div v-else class="stats-grid">
      <div class="stat-card">
        <h3>Total Emails</h3>
        <div class="stat-value">{{ stats.totalEmails }}</div>
        <div class="stat-growth">{{ stats.totalEmailsGrowth }}</div>
      </div>
      <!-- More stat cards -->
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const stats = ref(null);
const loading = ref(true);
const error = ref(null);

const fetchDashboardStats = async () => {
  try {
    const token = localStorage.getItem('authToken');
    const response = await fetch('/api/dashboard/stats', {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    const result = await response.json();
    
    if (result.status === '1') {
      stats.value = result.data;
    } else {
      error.value = result.message;
    }
  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchDashboardStats();
});
</script>
```

## Data Calculations

### Response Rate Calculation
```
Response Rate = (Total Responses / Total Emails) × 100

Response Statuses:
- EMAIL_RECEIVED
- CONTACTED_ON_PHONE  
- Interview_Scheduled
- HIRED
```

### Growth Percentage Calculation
```
Growth % = ((Current - Previous) / Previous) × 100

Examples:
- Current: 100, Previous: 80 → +25%
- Current: 75, Previous: 100 → -25%
- Current: 50, Previous: 0 → +100%
```

### Active Campaigns
Number of unique days with email activity in the last 30 days.

## Error Handling

All endpoints return consistent error responses:

```json
{
  "status": "0",
  "message": "Error description"
}
```

Common error scenarios:
- **401 Unauthorized**: Invalid or missing JWT token
- **403 Forbidden**: User doesn't have required permissions
- **400 Bad Request**: Invalid parameters (e.g., invalid date format, invalid period)
- **500 Internal Server Error**: Server-side error

## Performance Considerations

1. **Caching**: Consider implementing client-side caching for dashboard stats
2. **Refresh Intervals**: Don't refresh too frequently (recommended: every 5-10 minutes)
3. **Lazy Loading**: Load detailed stats only when needed
4. **Pagination**: For large datasets, consider implementing pagination

## Testing

Use the test page at `/dashboard-test.html` to verify API functionality:

1. Replace `'your-jwt-token-here'` with a valid JWT token
2. Open the page in your browser
3. Test all dashboard endpoints
4. Verify data accuracy and formatting

## Security

- All endpoints require authentication (`@PreAuthorize("hasRole('USER')")`)
- Data is filtered by the authenticated user's ID
- No sensitive information is exposed in responses
- All database queries use parameterized queries to prevent SQL injection