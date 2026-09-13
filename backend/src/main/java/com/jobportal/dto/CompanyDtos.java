package com.jobportal.dto;
import jakarta.validation.constraints.NotBlank;
public class CompanyDtos {
 public record CompanyRequest(@NotBlank String name,String description,String website,String location){}
 public record CompanyResponse(Long id,String name,String description,String website,String location,String imageUrl,boolean approved){}
 public record CompanyRequestResponse(Long id,String name,String description,String website,String location,String imageUrl,Long requestedById,String requestedByName,String status,String adminNote,String createdAt){}
}
