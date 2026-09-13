package com.jobportal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.dto.JobDtos;
import com.jobportal.dto.UserDtos;
import com.jobportal.entity.User;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.CloudinaryService;
import com.jobportal.service.CommentService;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository users;
    private final UserService userService;
    private final JobService jobs;
    private final ApplicationService apps;
    private final CloudinaryService cloudinary;
    private final ObjectMapper objectMapper;
    private final CommentService comments;

    public UserController(UserRepository users, UserService userService, JobService jobs,
                          ApplicationService apps, CloudinaryService cloudinary,
                          ObjectMapper objectMapper, CommentService comments) {
        this.users = users;
        this.userService = userService;
        this.jobs = jobs;
        this.apps = apps;
        this.cloudinary = cloudinary;
        this.objectMapper = objectMapper;
        this.comments = comments;
    }

    private User currentUser(Authentication authentication) {
        return users.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @GetMapping("/me")
    public UserDtos.UserResponse me(Authentication authentication) {
        return userService.response(currentUser(authentication));
    }

    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserDtos.UserResponse update(
            @RequestPart("data") String data,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            Authentication authentication) {
        try {
            UserDtos.ProfileRequest request =
                    objectMapper.readValue(data, UserDtos.ProfileRequest.class);
            User current = currentUser(authentication);
            String imageUrl = null;
            if (profileImage != null && !profileImage.isEmpty()) {
                imageUrl = cloudinary.uploadProfileImage(profileImage, current.getId());
            }
            return userService.update(current.getId(), request, imageUrl);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid profile data: " + e.getMessage(), e);
        }
    }

    @GetMapping("/jobs")
    public List<JobDtos.JobResponse> jobs(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String location) {
        return jobs.search(q, company, location).stream().map(jobs::response).toList();
    }

    @GetMapping("/jobs/{jobId}")
    public JobDtos.JobResponse job(@PathVariable Long jobId) { return jobs.response(jobs.get(jobId)); }

    @PostMapping("/jobs/{jobId}/apply")
    public UserDtos.ApplicationResponse apply(@PathVariable Long jobId, Authentication authentication) {
        return apps.apply(jobId, currentUser(authentication));
    }

    @GetMapping("/applications")
    public List<UserDtos.ApplicationResponse> applications(Authentication authentication) {
        return apps.byUser(currentUser(authentication).getId());
    }

    @GetMapping("/jobs/{jobId}/comments")
    public List<UserDtos.CommentResponse> comments(@PathVariable Long jobId) {
        return comments.byJob(jobId);
    }

    @PostMapping("/jobs/{jobId}/comments")
    public UserDtos.CommentResponse addComment(
            @PathVariable Long jobId,
            @RequestBody UserDtos.CommentRequest request,
            Authentication authentication) {
        return comments.add(jobId, currentUser(authentication), request.content());
    }

    @PostMapping("/premium")
    public UserDtos.UserResponse premium(
            @RequestBody UserDtos.SubscriptionRequest request,
            Authentication authentication) {
        return userService.subscribe(currentUser(authentication).getId(), request);
    }
}
