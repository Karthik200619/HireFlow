package com.jobportal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="users", uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class User {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private String name;
  @Column(nullable=false, unique=true) private String email;
  @Column(nullable=false) private String phoneNumber;
  private Integer age;
  private Double experience;
  private String address;
  private String qualification;
  @Column(length=1000) private String profileImageUrl;
  @Column(nullable=false) private String password;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role;
  @Column(nullable=false) private LocalDateTime createdAt;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="company_id") private Company company;

  public User() {}
  public User(Long id,String name,String email,String phoneNumber,Integer age,Double experience,String address,String qualification,String profileImageUrl,String password,Role role,LocalDateTime createdAt){this.id=id;this.name=name;this.email=email;this.phoneNumber=phoneNumber;this.age=age;this.experience=experience;this.address=address;this.qualification=qualification;this.profileImageUrl=profileImageUrl;this.password=password;this.role=role;this.createdAt=createdAt;}
  @PrePersist void prePersist(){if(createdAt==null)createdAt=LocalDateTime.now();if(role==null)role=Role.USER;}
  public Long getId(){return id;} public void setId(Long v){id=v;}
  public String getName(){return name;} public void setName(String v){name=v;}
  public String getEmail(){return email;} public void setEmail(String v){email=v;}
  public String getPhoneNumber(){return phoneNumber;} public void setPhoneNumber(String v){phoneNumber=v;}
  public Integer getAge(){return age;} public void setAge(Integer v){age=v;}
  public Double getExperience(){return experience;} public void setExperience(Double v){experience=v;}
  public String getAddress(){return address;} public void setAddress(String v){address=v;}
  public String getQualification(){return qualification;} public void setQualification(String v){qualification=v;}
  public String getProfileImageUrl(){return profileImageUrl;} public void setProfileImageUrl(String v){profileImageUrl=v;}
  public String getPassword(){return password;} public void setPassword(String v){password=v;}
  public Role getRole(){return role;} public void setRole(Role v){role=v;}
  public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
  public Company getCompany(){return company;} public void setCompany(Company v){company=v;}
  public enum Role { USER, RECRUITER, ADMIN }
}
