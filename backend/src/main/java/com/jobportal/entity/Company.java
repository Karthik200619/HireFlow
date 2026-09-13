package com.jobportal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="companies", uniqueConstraints=@UniqueConstraint(columnNames="name"))
public class Company {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true) private String name;
    @Column(length=4000) private String description;
    private String website;
    private String location;
    @Column(length=1000) private String imageUrl;
    @Column(nullable=false) private boolean approved;
    @Column(nullable=false) private LocalDateTime createdAt;

    public Company() {}
    public Company(Long id,String name,String description,String website,String location,String imageUrl,boolean approved,LocalDateTime createdAt){this.id=id;this.name=name;this.description=description;this.website=website;this.location=location;this.imageUrl=imageUrl;this.approved=approved;this.createdAt=createdAt;}
    @PrePersist void prePersist(){if(createdAt==null)createdAt=LocalDateTime.now();}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getWebsite(){return website;} public void setWebsite(String v){website=v;}
    public String getLocation(){return location;} public void setLocation(String v){location=v;}
    public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;}
    public boolean isApproved(){return approved;} public void setApproved(boolean v){approved=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}
