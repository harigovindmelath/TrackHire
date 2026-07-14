package com.trackhire.trackhire.repository;

import com.trackhire.trackhire.entity.JobApplication;
import com.trackhire.trackhire.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByCompanyNameContainingIgnoreCase(String companyName);

    List<JobApplication> findByStatus(Status status);
    List<JobApplication> findByUser_Id(Long userId);
}