package com.S_Health.GenderHealthCare.enums;

public enum PaymentMethod {
    VN_PAY,
    /**
     * Kept only for reading historical database records.
     * No active MoMo payment flow uses this value.
     */
    @Deprecated
    MOMO,
    PAY_OFF
}
