package com.trackhire.trackhire.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Column(nullable = false)
    private String companyName;

    @Setter
    @Column(nullable = false)
    private String role;

    @Setter
    private String skills;

    @Setter
    private String location;

    @Setter
    private String experienceRequired;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String jobDescriptionRaw;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String notes;

    @Setter
    @Enumerated(EnumType.STRING)
    private Status status;

    @Setter
    private LocalDateTime appliedDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public JobApplication() {
    }
}