package com.jobportal.dto;
import jakarta.validation.constraints.*;
public class JobDtos {
 public record JobRequest(@NotBlank String title,@NotBlank String company,@NotBlank String location,String employmentType,Double minSalary,Double maxSalary,String experienceRequired,String qualification,String description,String skills,boolean premium,Long companyId){}
 public record JobResponse(Long id,String title,String company,String location,String employmentType,Double minSalary,Double maxSalary,String experienceRequired,String qualification,String description,String skills,boolean premium,boolean active,String postedAt,Long recruiterId,String recruiterName,String recruiterProfileImageUrl,String approvalStatus,String adminNote,String rejectionReason,Long companyId,String companyDescription,String companyWebsite,String companyLocation,String companyImageUrl){}
}
