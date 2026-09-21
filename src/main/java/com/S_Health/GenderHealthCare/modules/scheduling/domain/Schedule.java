package com.S_Health.GenderHealthCare.modules.scheduling.domain;

import com.S_Health.GenderHealthCare.modules.scheduling.enums.ScheduleStatus;
import com.S_Health.GenderHealthCare.modules.user.domain.User;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    LocalDate workDate;
    LocalTime startTime;
    LocalTime endTime;
    boolean available;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultant_id")
    User consultant;
    @Enumerated(EnumType.STRING)
    ScheduleStatus status;
}
