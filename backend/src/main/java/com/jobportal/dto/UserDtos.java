package com.jobportal.dto;
public class UserDtos {
 public record UserResponse(Long id,String name,String email,String phoneNumber,Integer age,Double experience,String address,String qualification,String role,boolean premium,String profileImageUrl,Long companyId,String companyName){}
 public record ProfileRequest(String name,String phoneNumber,Integer age,Double experience,String address,String qualification){}
 public record SubscriptionRequest(String plan,int months){}
 public record ApplicationResponse(Long id,Long jobId,String jobTitle,String company,String location,String status,String appliedAt,Long userId,String userName,String userProfileImageUrl,String resumeUrl){}
 public record StatusRequest(String status){}
 public record CommentRequest(String content){}
 public record CommentResponse(Long id,Long jobId,Long userId,String userName,String userProfileImageUrl,String content,String createdAt){}
}
