package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "guest_visits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestVisitJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "check_in_log_id", nullable = false)
    private UUID checkInLogId;

    @Column(name = "visit_type", nullable = false, length = 20)
    private String visitType; // Trial, Guest_Of_Member

    @Column(name = "accompanied_by_member_id")
    private UUID accompaniedByMemberId;
}