package com.S_Health.GenderHealthCare.integrations.vnpay;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.integrations.IntegrationMessages;
import com.S_Health.GenderHealthCare.modules.payment.PaymentMessages;
import com.S_Health.GenderHealthCare.modules.payment.config.VNPayConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

/**
 * VNPay-specific URL, signature, and callback handling.
 */
@Service
public class VNPayGatewayAdapter implements VNPayGateway {
    private static final String SECURE_HASH_PARAMETER = "vnp_SecureHash";
    private static final String SECURE_HASH_TYPE_PARAMETER = "vnp_SecureHashType";
    private static final String RESPONSE_CODE_PARAMETER = "vnp_ResponseCode";
    private static final String TRANSACTION_REFERENCE_PARAMETER = "vnp_TxnRef";
    private static final String TRANSACTION_NUMBER_PARAMETER = "vnp_TransactionNo";
    private static final String TRANSACTION_STATUS_PARAMETER = "vnp_TransactionStatus";
    private static final String AMOUNT_PARAMETER = "vnp_Amount";
    private static final String PAYMENT_DATE_PARAMETER = "vnp_PayDate";
    private static final String DATE_PATTERN = "yyyyMMddHHmmss";

    private final VNPayConfig vnPayConfig;

    public VNPayGatewayAdapter(VNPayConfig vnPayConfig) {
        this.vnPayConfig = vnPayConfig;
    }

    @Override
    public VNPayPaymentLink createPaymentLink(
            BigDecimal amount,
            String orderInfo,
            int timeoutMinutes) {
        String transactionReference = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 20);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("vnp_Version", PaymentMessages.VNPAY_VERSION);
        parameters.put("vnp_Command", PaymentMessages.VNPAY_COMMAND);
        parameters.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        parameters.put(
                "vnp_Amount",
                amount.movePointRight(2).setScale(0, RoundingMode.HALF_UP).toPlainString());
        parameters.put("vnp_CreateDate", formatDate(new Date()));
        parameters.put("vnp_CurrCode", PaymentMessages.VNPAY_CURRENCY);
        parameters.put("vnp_IpAddr", PaymentMessages.LOCAL_IP_ADDRESS);
        parameters.put("vnp_Locale", PaymentMessages.VNPAY_LOCALE);
        parameters.put("vnp_OrderInfo", orderInfo);
        parameters.put("vnp_OrderType", PaymentMessages.VNPAY_ORDER_TYPE);
        parameters.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        parameters.put("vnp_ExpireDate", formatDate(expirationDate(timeoutMinutes)));
        parameters.put(TRANSACTION_REFERENCE_PARAMETER, transactionReference);

        String query = createSignedQuery(parameters);
        return new VNPayPaymentLink(
                transactionReference,
                vnPayConfig.getPayUrl() + "?" + query);
    }

    @Override
    public VNPayCallback verifyCallback(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (!SECURE_HASH_PARAMETER.equals(key)
                    && !SECURE_HASH_TYPE_PARAMETER.equals(key)
                    && values.length > 0) {
                parameters.put(key, values[0]);
            }
        });

        String receivedHash = request.getParameter(SECURE_HASH_PARAMETER);
        String calculatedHash = calculateSignature(parameters);
        if (receivedHash == null
                || !MessageDigest.isEqual(
                        calculatedHash.getBytes(StandardCharsets.US_ASCII),
                        receivedHash.getBytes(StandardCharsets.US_ASCII))) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.INVALID_VNPAY_SIGNATURE);
        }

        String responseCode = parameters.get(RESPONSE_CODE_PARAMETER);
        String transactionReference = parameters.get(TRANSACTION_REFERENCE_PARAMETER);
        if (responseCode == null || transactionReference == null) {
            throw new DomainException(ErrorCode.BAD_REQUEST, IntegrationMessages.VNPAY_CALLBACK_INVALID);
        }

        int resultCode;
        try {
            resultCode = Integer.parseInt(responseCode);
        } catch (NumberFormatException exception) {
            throw new DomainException(ErrorCode.BAD_REQUEST, IntegrationMessages.VNPAY_CALLBACK_INVALID, exception);
        }

        BigDecimal amount;
        try {
            String rawAmount = parameters.get(AMOUNT_PARAMETER);
            if (rawAmount == null) {
                throw new NumberFormatException("missing amount");
            }
            amount = new BigDecimal(rawAmount).movePointLeft(2);
        } catch (NumberFormatException exception) {
            throw new DomainException(ErrorCode.BAD_REQUEST, IntegrationMessages.VNPAY_CALLBACK_INVALID, exception);
        }

        return new VNPayCallback(
                transactionReference,
                parameters.get(TRANSACTION_NUMBER_PARAMETER),
                parameters.get(TRANSACTION_STATUS_PARAMETER),
                amount,
                resultCode,
                parsePaymentTime(parameters.get(PAYMENT_DATE_PARAMETER)));
    }

    private String createSignedQuery(Map<String, String> parameters) {
        List<String> fieldNames = new ArrayList<>(parameters.keySet());
        fieldNames.sort(String::compareTo);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String encodedValue = URLEncoder.encode(
                    parameters.get(fieldName),
                    StandardCharsets.US_ASCII);
            if (hashData.length() > 0) {
                hashData.append('&');
                query.append('&');
            }
            hashData.append(fieldName).append('=').append(encodedValue);
            query.append(fieldName).append('=').append(encodedValue);
        }

        query.append("&")
                .append(SECURE_HASH_PARAMETER)
                .append('=')
                .append(VNPayConfig.hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString()));
        return query.toString();
    }

    private String calculateSignature(Map<String, String> parameters) {
        List<String> fieldNames = new ArrayList<>(parameters.keySet());
        fieldNames.sort(String::compareTo);

        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            if (hashData.length() > 0) {
                hashData.append('&');
            }
            hashData.append(fieldName)
                    .append('=')
                    .append(URLEncoder.encode(parameters.get(fieldName), StandardCharsets.US_ASCII));
        }
        return VNPayConfig.hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
    }

    private Date expirationDate(int timeoutMinutes) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(PaymentMessages.VNPAY_TIME_ZONE));
        calendar.add(Calendar.MINUTE, timeoutMinutes);
        return calendar.getTime();
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat(DATE_PATTERN).format(date);
    }

    private LocalDateTime parsePaymentTime(String value) {
        if (value == null || !value.matches("\\d{14}")) {
            return LocalDateTime.now();
        }
        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(DATE_PATTERN));
        } catch (Exception exception) {
            return LocalDateTime.now();
        }
    }
}
