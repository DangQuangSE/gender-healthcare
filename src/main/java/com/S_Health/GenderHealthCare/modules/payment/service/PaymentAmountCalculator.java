package com.S_Health.GenderHealthCare.modules.payment.service;

import com.S_Health.GenderHealthCare.modules.payment.PaymentMessages;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentIntent;
import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PaymentAmountCalculator {

    public BigDecimal calculate(BigDecimal serviceAmount, PaymentIntent intent) {
        if (serviceAmount == null || serviceAmount.signum() <= 0 || intent == null) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.INVALID_PAYMENT_AMOUNT);
        }

        BigDecimal chargedAmount = intent == PaymentIntent.DEPOSIT
                ? serviceAmount.multiply(PaymentMessages.DEPOSIT_PAYMENT_RATE)
                : serviceAmount;

        return chargedAmount.setScale(0, RoundingMode.DOWN);
    }
}
