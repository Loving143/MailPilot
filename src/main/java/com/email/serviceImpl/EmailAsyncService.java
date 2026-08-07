package com.email.serviceImpl;

import com.email.entity.RecentEmail;
import com.email.request.QuickSendRequest;

public interface EmailAsyncService {
    void SendEmail(RecentEmail recentEmail, QuickSendRequest req);
}
