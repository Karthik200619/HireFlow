package com.jobportal.repository;
import com.jobportal.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CompanyRepository extends JpaRepository<Company,Long>{List<Company> findByApprovedTrueOrderByNameAsc(); Optional<Company> findByNameIgnoreCase(String name);}
