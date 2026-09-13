package com.jobportal.service;
import com.jobportal.dto.CompanyDtos;
import com.jobportal.entity.*;
import com.jobportal.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@Service @Transactional
public class CompanyService {
 private final CompanyRepository companies; private final CompanyRequestRepository requests; private final CloudinaryService cloudinary;
 public CompanyService(CompanyRepository companies,CompanyRequestRepository requests,CloudinaryService cloudinary){this.companies=companies;this.requests=requests;this.cloudinary=cloudinary;}
 @Transactional(readOnly=true) public List<CompanyDtos.CompanyResponse> approved(){return companies.findByApprovedTrueOrderByNameAsc().stream().map(this::response).toList();}
 @Transactional(readOnly=true) public Company getApproved(Long id){Company c=companies.findById(id).orElseThrow(()->new IllegalArgumentException("Company not found"));if(!c.isApproved())throw new IllegalArgumentException("Company is not approved");return c;}
 public Company create(CompanyDtos.CompanyRequest r, MultipartFile image){return createDirect(r,image);}
 public Company createDirect(CompanyDtos.CompanyRequest r, MultipartFile image){if(companies.findByNameIgnoreCase(r.name()).isPresent())throw new IllegalArgumentException("Company already exists");Company c=new Company(null,r.name(),r.description(),r.website(),r.location(),null,true,null);c=companies.save(c);if(image!=null&&!image.isEmpty()){c.setImageUrl(cloudinary.uploadCompanyImage(image,c.getId()));c=companies.save(c);}return c;}
 public CompanyRequest submit(CompanyDtos.CompanyRequest r, User recruiter, MultipartFile image){CompanyRequest q=new CompanyRequest(null,r.name(),r.description(),r.website(),r.location(),null,recruiter,CompanyRequest.Status.PENDING,null,null);q=requests.save(q);if(image!=null&&!image.isEmpty()){q.setImageUrl(cloudinary.uploadCompanyImage(image,q.getId()));q=requests.save(q);}return q;}
 @Transactional(readOnly=true) public List<CompanyDtos.CompanyRequestResponse> allRequests(){return requests.findAllByOrderByCreatedAtDesc().stream().map(this::requestResponse).toList();}
 public Company approveRequest(Long id){CompanyRequest q=requests.findById(id).orElseThrow(()->new IllegalArgumentException("Company request not found"));if(q.getStatus()!=CompanyRequest.Status.PENDING)throw new IllegalArgumentException("Request is already resolved");Company c=createDirect(new CompanyDtos.CompanyRequest(q.getName(),q.getDescription(),q.getWebsite(),q.getLocation()),null);c.setImageUrl(q.getImageUrl());c=companies.save(c);q.setStatus(CompanyRequest.Status.APPROVED);q.setAdminNote("Approved");requests.save(q);return c;}
 public void rejectRequest(Long id,String reason){CompanyRequest q=requests.findById(id).orElseThrow(()->new IllegalArgumentException("Company request not found"));q.setStatus(CompanyRequest.Status.REJECTED);q.setAdminNote(reason==null?"Rejected by admin":reason);requests.save(q);}
 public CompanyDtos.CompanyResponse response(Company c){return new CompanyDtos.CompanyResponse(c.getId(),c.getName(),c.getDescription(),c.getWebsite(),c.getLocation(),c.getImageUrl(),c.isApproved());}
 @Transactional(readOnly=true) public CompanyDtos.CompanyRequestResponse requestResponse(CompanyRequest q){return new CompanyDtos.CompanyRequestResponse(q.getId(),q.getName(),q.getDescription(),q.getWebsite(),q.getLocation(),q.getImageUrl(),q.getRequestedBy().getId(),q.getRequestedBy().getName(),q.getStatus().name(),q.getAdminNote(),q.getCreatedAt()==null?null:q.getCreatedAt().toString());}
}
