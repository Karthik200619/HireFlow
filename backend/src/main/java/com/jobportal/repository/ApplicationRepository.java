package com.jobportal.repository;
import com.jobportal.entity.Application; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ApplicationRepository extends JpaRepository<Application,Long>{ List<Application> findByUserIdOrderByAppliedAtDesc(Long id); List<Application> findByJobRecruiterIdOrderByAppliedAtDesc(Long id); boolean existsByJobIdAndUserId(Long jobId,Long userId); long countByStatus(Application.Status s); }
