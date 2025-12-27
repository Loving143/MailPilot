package com.email.service;

import java.util.List;

import com.email.master.IntentCode;
import com.email.request.IntentCodeRequest;
import com.email.resposne.IntentCodeResponse;

public interface IntentCodeService {

	IntentCode registerIntentCode(IntentCodeRequest req);

	IntentCodeResponse fetchIntentCode(String intentCode);

	List<IntentCode> getIntentsByCategory(String category);

}
