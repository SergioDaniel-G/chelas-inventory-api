package com.micheladas.chelas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_ips")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserIp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;

    private String device;

    @Column(length = 500)
    private String userAgent;

    private Boolean isBot;
    private String riskLevel;

    private String status;
    private String failureReason;

    @CreationTimestamp
    private LocalDateTime loginTime;

}
