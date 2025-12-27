package com.email.service;

import com.email.constants.EmailStatus;
import com.email.request.EmailIntentRequest;
import com.email.request.HrDetailsRequest;
import com.email.request.QuickSendRequest;

import jakarta.mail.MessagingException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface EmailService {

	public void send(HrDetailsRequest req) ;

    void updateEmailStatus(String  email, EmailStatus status,String mobNo) ;

    ByteArrayInputStream generateExcel() throws IOException;

    void sendExcel(String mail, byte[] bytes) throws MessagingException;

    void addHrDetails(HrDetailsRequest req);

	public void saveEmailLog(HrDetailsRequest req);

	public void quickSend(QuickSendRequest req);

	public void sendIntentEmail(EmailIntentRequest req);
}
