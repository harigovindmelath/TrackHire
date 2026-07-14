package com.trackhire.trackhire.controller;

import com.trackhire.trackhire.entity.JobApplication;
import com.trackhire.trackhire.entity.Status;
import com.trackhire.trackhire.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;

    @PostMapping
    public JobApplication createApplication(@RequestParam Long userId, @RequestBody JobApplication application) {
        return jobApplicationService.createApplication(userId, application);
    }
    @GetMapping("/{id}")
    public JobApplication getApplication(@PathVariable Long id) {
        return jobApplicationService.getApplicationById(id);
    }
    @PutMapping("/{id}/status")
    public JobApplication updateStatus(@PathVariable Long id, @RequestParam Status newStatus){
        return jobApplicationService.updateStatus(id, newStatus);
    }
    @GetMapping("/search")
    public List<JobApplication> searchByCompany(@RequestParam String company){
        return jobApplicationService.searchByCompany(company);
    }
    @GetMapping("/filter")
    public List<JobApplication> filterByStatus(@RequestParam Status status){
        return jobApplicationService.filterByStatus(status);
    }
    @GetMapping("/user/{userId}")
    public  List<JobApplication> getApplicationsForUser(@RequestParam Long userId){
        return jobApplicationService.getApplicationsForUser(userId);
    }

}