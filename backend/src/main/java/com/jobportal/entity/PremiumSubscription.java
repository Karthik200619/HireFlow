package com.jobportal.entity;
import jakarta.persistence.*; import java.time.LocalDate;
@Entity @Table(name="premium_subscriptions") public class PremiumSubscription {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @OneToOne @JoinColumn(name="user_id",nullable=false,unique=true) private User user; @Column(nullable=false) private LocalDate startDate; @Column(nullable=false) private LocalDate endDate; @Enumerated(EnumType.STRING) private Plan plan;
 public PremiumSubscription(){} public PremiumSubscription(Long id,User user,LocalDate startDate,LocalDate endDate,Plan plan){this.id=id;this.user=user;this.startDate=startDate;this.endDate=endDate;this.plan=plan;}
 public boolean active(){return !endDate.isBefore(LocalDate.now());} public Long getId(){return id;} public void setId(Long v){id=v;} public User getUser(){return user;} public void setUser(User v){user=v;} public LocalDate getStartDate(){return startDate;} public void setStartDate(LocalDate v){startDate=v;} public LocalDate getEndDate(){return endDate;} public void setEndDate(LocalDate v){endDate=v;} public Plan getPlan(){return plan;} public void setPlan(Plan v){plan=v;}
 public enum Plan { MONTHLY, QUARTERLY, YEARLY }
}
