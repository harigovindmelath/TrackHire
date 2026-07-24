package com.trackhire.trackhire.controller;

import com.trackhire.trackhire.client.ExtractedJobDetails;
import com.trackhire.trackhire.client.GroqClient;
import com.trackhire.trackhire.client.GroqResponseParser;
import com.trackhire.trackhire.dto.ConfirmApplicationRequest;
import com.trackhire.trackhire.dto.JobDescriptionRequest;
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

    @Autowired
    private GroqClient groqClient;

    @Autowired
    private GroqResponseParser groqResponseParser;

    @PostMapping
    public JobApplication createApplication(@RequestBody JobApplication application) {
        return jobApplicationService.createApplication(application);
    }

    @GetMapping("/{id}")
    public JobApplication getApplication(@PathVariable Long id) {
        return jobApplicationService.getApplicationById(id);
    }

    @PutMapping("/{id}/status")
    public JobApplication updateStatus(@PathVariable Long id, @RequestParam Status newStatus) {
        return jobApplicationService.updateStatus(id, newStatus);
    }

    @GetMapping("/search")
    public List<JobApplication> searchByCompany(@RequestParam String company) {
        return jobApplicationService.searchByCompany(company);
    }

    @GetMapping("/filter")
    public List<JobApplication> filterByStatus(@RequestParam Status status) {
        return jobApplicationService.filterByStatus(status);
    }

    @GetMapping("/mine")
    public List<JobApplication> getMyApplications() {
        return jobApplicationService.getApplicationsForCurrentUser();
    }

    @PostMapping("/parse")
    public ExtractedJobDetails parseJobDescription(@RequestBody JobDescriptionRequest request) {
        String rawContent = groqClient.extractJobDetails(request.getJobDescriptionText());
        return groqResponseParser.parse(rawContent);
    }

    @PostMapping("/confirm")
    public JobApplication confirmApplication(@RequestBody ConfirmApplicationRequest request) {
        return jobApplicationService.createApplicationFromExtraction(request);
    }
}