package com.fitness.core.auth.port.out;

public interface IEmailSenderPort {
    void sendProfessionalHtmlEmail(String toAddress, String subject, String htmlBody);
}