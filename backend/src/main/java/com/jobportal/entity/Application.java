package com.jobportal.entity;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="applications",uniqueConstraints=@UniqueConstraint(columnNames={"job_id","user_id"})) public class Application {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="job_id",nullable=false) private Job job; @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="user_id",nullable=false) private User user; @Enumerated(EnumType.STRING) private Status status; private LocalDateTime appliedAt;
 public Application(){} public Application(Long id,Job job,User user,Status status,LocalDateTime appliedAt){this.id=id;this.job=job;this.user=user;this.status=status;this.appliedAt=appliedAt;}
 @PrePersist void prePersist(){if(appliedAt==null)appliedAt=LocalDateTime.now();if(status==null)status=Status.APPLIED;}
 public Long getId(){return id;} public void setId(Long v){id=v;} public Job getJob(){return job;} public void setJob(Job v){job=v;} public User getUser(){return user;} public void setUser(User v){user=v;} public Status getStatus(){return status;} public void setStatus(Status v){status=v;} public LocalDateTime getAppliedAt(){return appliedAt;} public void setAppliedAt(LocalDateTime v){appliedAt=v;}
 public enum Status { APPLIED, REVIEWING, SHORTLISTED, REJECTED, HIRED }
}
