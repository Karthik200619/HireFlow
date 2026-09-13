package com.jobportal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="company_requests")
public class CompanyRequest {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    @Column(length=4000) private String description;
    private String website;
    private String location;
    @Column(length=1000) private String imageUrl;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="requested_by",nullable=false) private User requestedBy;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Status status;
    @Column(length=1000) private String adminNote;
    @Column(nullable=false) private LocalDateTime createdAt;
    public CompanyRequest() {}
    public CompanyRequest(Long id,String name,String description,String website,String location,String imageUrl,User requestedBy,Status status,String adminNote,LocalDateTime createdAt){this.id=id;this.name=name;this.description=description;this.website=website;this.location=location;this.imageUrl=imageUrl;this.requestedBy=requestedBy;this.status=status;this.adminNote=adminNote;this.createdAt=createdAt;}
    @PrePersist void prePersist(){if(createdAt==null)createdAt=LocalDateTime.now();if(status==null)status=Status.PENDING;}
    public Long getId(){return id;} public String getName(){return name;} public String getDescription(){return description;} public String getWebsite(){return website;} public String getLocation(){return location;} public String getImageUrl(){return imageUrl;} public User getRequestedBy(){return requestedBy;} public Status getStatus(){return status;} public String getAdminNote(){return adminNote;} public LocalDateTime getCreatedAt(){return createdAt;}
    public void setId(Long v){id=v;} public void setName(String v){name=v;} public void setDescription(String v){description=v;} public void setWebsite(String v){website=v;} public void setLocation(String v){location=v;} public void setImageUrl(String v){imageUrl=v;} public void setRequestedBy(User v){requestedBy=v;} public void setStatus(Status v){status=v;} public void setAdminNote(String v){adminNote=v;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
    public enum Status { PENDING, APPROVED, REJECTED }
}
