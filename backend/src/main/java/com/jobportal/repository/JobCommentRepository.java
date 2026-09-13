package com.jobportal.repository;
import com.jobportal.entity.JobComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface JobCommentRepository extends JpaRepository<JobComment,Long>{ List<JobComment> findByJobIdOrderByCreatedAtDesc(Long jobId); }
