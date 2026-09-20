package com.S_Health.GenderHealthCare.modules.payment.domain;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionPayOSFieldsContractTest {

    @Test
    void storesPayOSOrderIdentityAndActualChargedAmount() throws Exception {
        Field providerOrderCode = Transaction.class.getDeclaredField("providerOrderCode");
        Field providerPaymentLinkId = Transaction.class.getDeclaredField("providerPaymentLinkId");
        Field providerCheckoutUrl = Transaction.class.getDeclaredField("providerCheckoutUrl");
        Field chargedAmount = Transaction.class.getDeclaredField("chargedAmount");

        assertThat(providerOrderCode.getType()).isEqualTo(Long.class);
        assertThat(providerPaymentLinkId.getType()).isEqualTo(String.class);
        assertThat(providerCheckoutUrl.getType()).isEqualTo(String.class);
        assertThat(chargedAmount.getType()).isEqualTo(BigDecimal.class);
    }
}
