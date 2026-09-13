package com.jobportal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="job_comments")
public class JobComment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="job_id",nullable=false) private Job job;
 @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="user_id",nullable=false) private User user;
 @Column(nullable=false,length=1000) private String content;
 @Column(nullable=false) private LocalDateTime createdAt;
 public JobComment(){}
 public JobComment(Long id,Job job,User user,String content,LocalDateTime createdAt){this.id=id;this.job=job;this.user=user;this.content=content;this.createdAt=createdAt;}
 @PrePersist void prePersist(){if(createdAt==null)createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public Job getJob(){return job;} public void setJob(Job v){job=v;}
 public User getUser(){return user;} public void setUser(User v){user=v;}
 public String getContent(){return content;} public void setContent(String v){content=v;}
 public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}
