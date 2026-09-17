package com.S_Health.GenderHealthCare.integrations.mail;

import com.S_Health.GenderHealthCare.integrations.IntegrationMessages;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendOtp(String toEmail, String otp) {
        try {
            Context context = new Context();
            context.setVariable("name", toEmail);
            context.setVariable("otp", otp);
            context.setVariable("messageLine1", IntegrationMessages.OTP_EMAIL_MESSAGE);

            String html = templateEngine.process("emailotp", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(IntegrationMessages.OTP_EMAIL_SUBJECT);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error(IntegrationMessages.EMAIL_SEND_FAILED.formatted(e.getMessage()), e);
        }
    }

    public void sendForgotPasswordOtp(String toEmail, String otp) {
        try {
            Context context = new Context();
            context.setVariable("name", toEmail);
            context.setVariable("otp", otp);
            context.setVariable("messageLine1", IntegrationMessages.PASSWORD_RESET_EMAIL_MESSAGE);

            String html = templateEngine.process("emailotp", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(IntegrationMessages.PASSWORD_RESET_EMAIL_SUBJECT);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error(IntegrationMessages.EMAIL_PASSWORD_RESET_FAILED.formatted(e.getMessage()), e);
        }
    }

    public void sendWelcome(String toEmail) {
        try {
            Context context = new Context();
            context.setVariable("name", toEmail);

            String html = templateEngine.process("emailwelcome", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(IntegrationMessages.WELCOME_EMAIL_SUBJECT);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error(IntegrationMessages.EMAIL_WELCOME_FAILED.formatted(e.getMessage()), e);
        }
    }

    public void sendWelcomeWithCredentials(String email, String randomPassword, UserRole role) {
        try {
            Context context = new Context();
            context.setVariable("email", email);
            context.setVariable("password", randomPassword);
            context.setVariable("role", role.name());

            String html = templateEngine.process("welcome-credentials", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject(IntegrationMessages.ACCOUNT_CREDENTIALS_EMAIL_SUBJECT.formatted(role.name()));
            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error(IntegrationMessages.EMAIL_SEND_FAILED.formatted(e.getMessage()), e);
        }
    }

    public void sendUrlCurtomerZoom(String toEmail, String startTime, String joinUrl, String serviceName) {
        try {
            Context context = new Context();
            context.setVariable("name", toEmail);
            context.setVariable("startTime", startTime);
            context.setVariable("joinUrl", joinUrl);
            context.setVariable("serviceName", serviceName);

            String html = templateEngine.process("emailZoom", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(IntegrationMessages.ZOOM_EMAIL_SUBJECT);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error(IntegrationMessages.EMAIL_ZOOM_CUSTOMER_FAILED.formatted(e.getMessage()), e);
        }
    }

    public void sendUrlConsultantZoom(String toEmail, String startTime, String startUrl, String serviceName) {
        try {
            Context context = new Context();
            context.setVariable("name", toEmail);
            context.setVariable("startTime", startTime);
            context.setVariable("startUrl", startUrl);
            context.setVariable("serviceName", serviceName);

            String html = templateEngine.process("ConsultantZoom", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(IntegrationMessages.ZOOM_EMAIL_SUBJECT);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error(IntegrationMessages.EMAIL_ZOOM_CONSULTANT_FAILED.formatted(e.getMessage()), e);
        }
    }

    public void sendAppointmentReminder(String toEmail, LocalDate date) {
        try {
            Context context = new Context();
            context.setVariable("customerName", toEmail);
            context.setVariable("appointmentDate", date);

            String html = templateEngine.process("appointment-reminder", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(IntegrationMessages.APPOINTMENT_REMINDER_EMAIL_SUBJECT);
            helper.setText(html, true);
            mailSender.send(message);
            log.info(IntegrationMessages.EMAIL_REMINDER_SENT.formatted(toEmail));
        } catch (Exception e) {
            log.error(IntegrationMessages.EMAIL_REMINDER_FAILED.formatted(e.getMessage()), e);
        }
    }
}
