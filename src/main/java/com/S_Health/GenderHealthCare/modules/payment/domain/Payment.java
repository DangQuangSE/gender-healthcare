package com.S_Health.GenderHealthCare.modules.payment.domain;

import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentStatus;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentMethod;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentIntent;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    PaymentMethod method;

    @Enumerated(EnumType.STRING)
    PaymentIntent paymentIntent;

    @Enumerated(EnumType.STRING)
    PaymentStatus status;

    BigDecimal amount;

    @CreationTimestamp
    LocalDateTime createdAt;

    LocalDateTime paidAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "appointment_id")
    Appointment appointment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paid_by_user_id")
    User paidBy;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private Transaction transaction;

}
