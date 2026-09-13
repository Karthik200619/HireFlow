package com.jobportal.service;
import com.jobportal.dto.UserDtos; import com.jobportal.entity.*; import com.jobportal.repository.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.*;
@Service @Transactional public class CommentService {
 private final JobCommentRepository comments; private final JobRepository jobs;
 public CommentService(JobCommentRepository comments,JobRepository jobs){this.comments=comments;this.jobs=jobs;}
 public UserDtos.CommentResponse add(Long jobId, User user, String content){if(content==null||content.isBlank())throw new IllegalArgumentException("Comment cannot be empty");if(content.length()>1000)throw new IllegalArgumentException("Comment must be 1000 characters or less");Job job=jobs.findById(jobId).orElseThrow(()->new IllegalArgumentException("Job not found"));var c=comments.save(new JobComment(null,job,user,content.trim(),null));return response(c);}
 @Transactional(readOnly = true) public List<UserDtos.CommentResponse> byJob(Long jobId){return comments.findByJobIdOrderByCreatedAtDesc(jobId).stream().map(this::response).toList();}
 @Transactional(readOnly = true) public List<UserDtos.CommentResponse> all(){return comments.findAll().stream().map(this::response).toList();}
 public void delete(Long id){if(!comments.existsById(id))throw new IllegalArgumentException("Comment not found");comments.deleteById(id);}
 public UserDtos.CommentResponse response(JobComment c){return new UserDtos.CommentResponse(c.getId(),c.getJob().getId(),c.getUser().getId(),c.getUser().getName(),c.getUser().getProfileImageUrl(),c.getContent(),c.getCreatedAt().toString());}
}
