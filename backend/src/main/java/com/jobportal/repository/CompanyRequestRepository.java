package com.jobportal.repository;
import com.jobportal.entity.CompanyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CompanyRequestRepository extends JpaRepository<CompanyRequest,Long>{List<CompanyRequest> findAllByOrderByCreatedAtDesc(); List<CompanyRequest> findByRequestedByIdOrderByCreatedAtDesc(Long id);}
