package com.trackhire.trackhire.service;

import com.trackhire.trackhire.dto.ConfirmApplicationRequest;
import com.trackhire.trackhire.entity.JobApplication;
import com.trackhire.trackhire.entity.Status;
import com.trackhire.trackhire.entity.User;
import com.trackhire.trackhire.exception.ResourceNotFoundException;
import com.trackhire.trackhire.repository.JobApplicationRepository;
import com.trackhire.trackhire.repository.UserRepository;
import com.trackhire.trackhire.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUserService currentUserService;

    public JobApplication createApplication(JobApplication application) {
        User currentUser = currentUserService.getCurrentUser();
        application.setUser(currentUser);
        application.setStatus(Status.SAVED);
        return jobApplicationRepository.save(application);
    }

    public JobApplication getApplicationById(Long id) {
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication not found with id: " + id));
        verifyOwnership(application);
        return application;
    }

    public JobApplication updateStatus(Long id, Status newStatus) {
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication not found with id: " + id));
        verifyOwnership(application);
        application.setStatus(newStatus);
        return jobApplicationRepository.save(application);
    }

    public List<JobApplication> searchByCompany(String companyName) {
        User currentUser = currentUserService.getCurrentUser();
        return jobApplicationRepository.findByCompanyNameContainingIgnoreCase(companyName)
                .stream()
                .filter(app -> app.getUser().getId().equals(currentUser.getId()))
                .collect(Collectors.toList());
    }

    public List<JobApplication> filterByStatus(Status status) {
        User currentUser = currentUserService.getCurrentUser();
        return jobApplicationRepository.findByStatus(status)
                .stream()
                .filter(app -> app.getUser().getId().equals(currentUser.getId()))
                .collect(Collectors.toList());
    }

    public List<JobApplication> getApplicationsForCurrentUser() {
        User currentUser = currentUserService.getCurrentUser();
        return jobApplicationRepository.findByUser_Id(currentUser.getId());
    }

    public JobApplication createApplicationFromExtraction(ConfirmApplicationRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        JobApplication application = new JobApplication();
        application.setUser(currentUser);
        application.setCompanyName(request.getCompanyName());
        application.setRole(request.getRole());
        application.setSkills(request.getSkills());
        application.setLocation(request.getLocation());
        application.setExperienceRequired(request.getExperienceRequired());
        application.setJobDescriptionRaw(request.getJobDescriptionRaw());
        application.setStatus(Status.SAVED);

        return jobApplicationRepository.save(application);
    }

    private void verifyOwnership(JobApplication application) {
        User currentUser = currentUserService.getCurrentUser();
        if (!application.getUser().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("JobApplication not found with id: " + application.getId());
        }
    }
}