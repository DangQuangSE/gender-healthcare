package com.S_Health.GenderHealthCare.modules.identity.service;



import com.S_Health.GenderHealthCare.integrations.mail.EmailService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OTPService {
    private final CacheManager cacheManager;
    private final EmailService emailService;

    public OTPService(CacheManager cacheManager, EmailService emailService) {
        this.cacheManager = cacheManager;
        this.emailService = emailService;
    }

    public void generateOTP(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        Cache cache = cacheManager.getCache("otpCache");
        cache.put(email, otp);
        emailService.sendOtp(email, otp);
    }

    public boolean verifyOtp(String email, String otp) {
        Cache cache = cacheManager.getCache("otpCache");
        String cachedOtp = cache.get(email, String.class);
        return otp.equals(cachedOtp);
    }

    public void generateForgotPasswordOTP(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        Cache cache = cacheManager.getCache("otpCache");
        cache.put(email, otp);
        emailService.sendForgotPasswordOtp(email, otp);
    }

    public void removeOtp(String email) {
        cacheManager.getCache("otpCache").evict(email);
    }
}
