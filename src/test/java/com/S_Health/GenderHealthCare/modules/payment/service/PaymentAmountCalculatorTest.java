package com.S_Health.GenderHealthCare.modules.payment.service;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentAmountCalculatorTest {

    private static final String CALCULATOR_CLASS =
            "com.S_Health.GenderHealthCare.modules.payment.service.PaymentAmountCalculator";
    private static final String INTENT_CLASS =
            "com.S_Health.GenderHealthCare.modules.payment.enums.PaymentIntent";

    @Test
    void calculatesFullPaymentWithoutChangingTheServiceAmount() throws Exception {
        Object actual = calculate("100000", "FULL");

        assertThat(actual).isEqualTo(new BigDecimal("100000"));
    }

    @Test
    void calculatesTheApprovedTwentyPercentDeposit() throws Exception {
        Object actual = calculate("100001", "DEPOSIT");

        assertThat(actual).isEqualTo(new BigDecimal("20000"));
    }

    private Object calculate(String serviceAmount, String intentName) throws Exception {
        Class<?> intentType = Class.forName(INTENT_CLASS);
        @SuppressWarnings("unchecked")
        Object intent = Enum.valueOf((Class<? extends Enum>) intentType.asSubclass(Enum.class), intentName);
        Class<?> calculatorType = Class.forName(CALCULATOR_CLASS);
        Constructor<?> constructor = calculatorType.getDeclaredConstructor();
        Object calculator = constructor.newInstance();
        Method calculate = calculatorType.getMethod("calculate", BigDecimal.class, intentType);

        return calculate.invoke(calculator, new BigDecimal(serviceAmount), intent);
    }
}
