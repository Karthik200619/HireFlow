package com.jobportal.controller;

import com.jobportal.dto.JobDtos;
import com.jobportal.dto.UserDtos;
import com.jobportal.entity.User;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.CommentService;
import com.jobportal.service.JobService; import com.jobportal.service.CompanyService; import com.jobportal.dto.CompanyDtos;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiter")
public class RecruiterController {
    private final UserRepository users;
    private final JobService jobs;
    private final ApplicationService apps;
    private final CommentService comments; private final CompanyService companies;

    public RecruiterController(UserRepository users, JobService jobs,
                               ApplicationService apps, CommentService comments, CompanyService companies) {
        this.users = users;
        this.jobs = jobs;
        this.apps = apps;
        this.comments = comments; this.companies = companies;
    }

    private User currentUser(Authentication authentication) {
        return users.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Recruiter not found"));
    }

    @GetMapping("/jobs")
    public List<JobDtos.JobResponse> myJobs(Authentication authentication) {
        return jobs.recruiterJobs(currentUser(authentication).getId())
                .stream().map(jobs::response).toList();
    }

    @PostMapping("/jobs")
    public JobDtos.JobResponse create(@Valid @RequestBody JobDtos.JobRequest request,
                                      Authentication authentication) {
        return jobs.response(jobs.save(request, currentUser(authentication)));
    }

    @PutMapping("/jobs/{id}")
    public JobDtos.JobResponse update(@PathVariable Long id,
                                      @Valid @RequestBody JobDtos.JobRequest request,
                                      Authentication authentication) {
        return jobs.response(jobs.update(id, request, currentUser(authentication)));
    }

    @DeleteMapping("/jobs/{id}")
    public void delete(@PathVariable Long id, Authentication authentication) {
        jobs.delete(id, currentUser(authentication));
    }


    @GetMapping("/companies") public List<CompanyDtos.CompanyResponse> companies(){ return companies.approved(); }
    @PostMapping(value="/companies/request",consumes="multipart/form-data") public CompanyDtos.CompanyRequestResponse requestCompany(@RequestPart("data") String data,@RequestPart(value="image",required=false) org.springframework.web.multipart.MultipartFile image,Authentication authentication) throws Exception { var r=new com.fasterxml.jackson.databind.ObjectMapper().readValue(data,CompanyDtos.CompanyRequest.class); return companies.requestResponse(companies.submit(r,currentUser(authentication),image)); }

    @GetMapping("/applications")
    public List<UserDtos.ApplicationResponse> applications(Authentication authentication) {
        return apps.byRecruiter(currentUser(authentication).getId());
    }

    @PutMapping("/applications/{id}/status")
    public UserDtos.ApplicationResponse status(@PathVariable Long id,
                                               @Valid @RequestBody UserDtos.StatusRequest request,
                                               Authentication authentication) {
        return apps.status(id, request.status(), currentUser(authentication));
    }

    @GetMapping("/jobs/{jobId}/comments")
    public List<UserDtos.CommentResponse> jobComments(@PathVariable Long jobId,
                                                       Authentication authentication) {
        User recruiter = currentUser(authentication);
        jobs.verifyRecruiterOwnsJob(jobId, recruiter.getId());
        return comments.byJob(jobId);
    }
}
