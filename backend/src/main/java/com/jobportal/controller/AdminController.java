package com.jobportal.controller;
import com.jobportal.dto.*; import com.jobportal.entity.*; import com.jobportal.repository.*; import com.jobportal.service.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/admin") public class AdminController { private final UserRepository users; private final JobRepository jobs; private final ApplicationService apps; private final JobService jobService; private final UserService userService; private final CommentService comments; private final CompanyService companies; public AdminController(UserRepository users, JobRepository jobs, ApplicationService apps, JobService jobService, UserService userService, CommentService comments, CompanyService companies){this.users=users;this.jobs=jobs;this.apps=apps;this.jobService=jobService;this.userService=userService;this.comments=comments;this.companies=companies;}
 @GetMapping("/dashboard") public Map<String,Object> dashboard(){return Map.of("users",users.count(),"recruiters",users.countByRole(User.Role.RECRUITER),"jobs",jobService.count(),"activeJobs",jobService.activeCount(),"applications",apps.count(),"hired",apps.statusCount(Application.Status.HIRED));}
 @GetMapping("/users") public List<UserDtos.UserResponse> allUsers(){return users.findAll().stream().map(userService::response).toList();}
 @GetMapping("/jobs") public List<JobDtos.JobResponse> allJobs(){return jobs.findAll().stream().map(jobService::response).toList();}
 @DeleteMapping("/users/{id}") public void deleteUser(@PathVariable Long id){users.deleteById(id);}
 @DeleteMapping("/jobs/{id}") public void deleteJob(@PathVariable Long id){jobs.deleteById(id);}
 @GetMapping("/applications") public List<UserDtos.ApplicationResponse> allApplications(){return apps.all();}
 @GetMapping("/comments") public List<UserDtos.CommentResponse> allComments(){return comments.all();}
 @DeleteMapping("/comments/{id}") public void deleteComment(@PathVariable Long id){comments.delete(id);}
 @PutMapping("/jobs/{id}/approve") public JobDtos.JobResponse approveJob(@PathVariable Long id){var j=jobService.get(id);j.setApprovalStatus(Job.ApprovalStatus.APPROVED);j.setActive(true);j.setRejectionReason(null);jobs.save(j);return jobService.response(j);}
 @PutMapping("/jobs/{id}/reject") public JobDtos.JobResponse rejectJob(@PathVariable Long id,@RequestBody Map<String,String> body){var j=jobService.get(id);j.setApprovalStatus(Job.ApprovalStatus.REJECTED);j.setActive(false);j.setRejectionReason(body.getOrDefault("reason","Rejected by admin"));jobs.save(j);return jobService.response(j);}

 @GetMapping("/companies") public List<CompanyDtos.CompanyResponse> companies(){return companies.approved();}
 @GetMapping("/company-requests") public List<CompanyDtos.CompanyRequestResponse> companyRequests(){return companies.allRequests();}
 @PostMapping(value="/companies",consumes="multipart/form-data") public CompanyDtos.CompanyResponse createCompany(@RequestPart("data") String data,@RequestPart(value="image",required=false) org.springframework.web.multipart.MultipartFile image)throws Exception{return companies.response(companies.createDirect(new com.fasterxml.jackson.databind.ObjectMapper().readValue(data,CompanyDtos.CompanyRequest.class),image));}
 @PutMapping("/company-requests/{id}/approve") public CompanyDtos.CompanyResponse approveCompany(@PathVariable Long id){return companies.response(companies.approveRequest(id));}
 @PutMapping("/company-requests/{id}/reject") public void rejectCompany(@PathVariable Long id,@RequestBody Map<String,String> body){companies.rejectRequest(id,body.get("reason"));}
}
