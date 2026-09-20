package com.S_Health.GenderHealthCare.modules.payment.enums;

public enum PaymentMethod {
    PAYOS,
    VN_PAY,
    /**
     * Kept only for reading historical database records.
     * No active MoMo payment flow uses this value.
     */
    @Deprecated
    MOMO,
    PAY_OFF
}
