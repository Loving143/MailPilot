package com.email.service;

import com.email.request.ProfileUpdateRequest;
import com.email.resposne.UserProfileResponse;

public interface PersonService {
    void logOutUser(String authHeader);

    UserProfileResponse fetchCurrentUserProfile();

    void updateProfile(ProfileUpdateRequest req);
}
