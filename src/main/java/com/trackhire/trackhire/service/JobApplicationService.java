package com.trackhire.trackhire.service;

import com.trackhire.trackhire.dto.ConfirmApplicationRequest;
import com.trackhire.trackhire.entity.JobApplication;
import com.trackhire.trackhire.entity.Status;
import com.trackhire.trackhire.entity.User;
import com.trackhire.trackhire.repository.JobApplicationRepository;
import com.trackhire.trackhire.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private UserRepository userRepository;


    public JobApplication createApplication(Long userId, JobApplication application) {
        User user = userRepository.findById(userId).orElse(null);
        application.setUser(user);
        application.setStatus(Status.SAVED);
        return jobApplicationRepository.save(application);
    }
    public JobApplication getApplicationById(Long id){
        return jobApplicationRepository.findById(id).orElse(null);
    }
    public JobApplication updateStatus(Long id, Status newStatus){
        JobApplication jobApplication = jobApplicationRepository.findById(id).orElse(null);
        jobApplication.setStatus(newStatus);
        return jobApplicationRepository.save(jobApplication);
    }
    public List<JobApplication> searchByCompany(String companyName) {
        return jobApplicationRepository.findByCompanyNameContainingIgnoreCase(companyName);
    }

    public List<JobApplication> filterByStatus(Status status) {
        return jobApplicationRepository.findByStatus(status);
    }
    public List<JobApplication> getApplicationsForUser(Long userId){
        return jobApplicationRepository.findByUser_Id(userId);
    }
    public JobApplication createApplicationFromExtraction(ConfirmApplicationRequest request) {
        User user = userRepository.findById(request.getUserId()).orElse(null);

        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setCompanyName(request.getCompanyName());
        application.setRole(request.getRole());
        application.setSkills(request.getSkills());
        application.setLocation(request.getLocation());
        application.setExperienceRequired(request.getExperienceRequired());
        application.setJobDescriptionRaw(request.getJobDescriptionRaw());
        application.setStatus(Status.SAVED);

        return jobApplicationRepository.save(application);
    }

}