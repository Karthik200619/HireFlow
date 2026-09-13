package com.jobportal.config;

import com.jobportal.entity.User;
import com.jobportal.repository.UserRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.entity.Job;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
 @Bean
 CommandLineRunner ensurePredefinedAdmin(UserRepository users, JobRepository jobs, PasswordEncoder encoder,
     @Value("${app.admin.email:admin@hireflow.com}") String email,
     @Value("${app.admin.password:Admin@123}") String password) {
   return args -> {
     var existing=users.findByEmail(email).orElse(null);
     if(existing==null){
       User u=new User(null,"HireFlow Admin",email,"9999999999",30,5.0,"Hyderabad","Administration",null,encoder.encode(password),User.Role.ADMIN,null);
       users.save(u);
     } else if(existing.getRole()!=User.Role.ADMIN || !encoder.matches(password,existing.getPassword())) {
       existing.setRole(User.Role.ADMIN); existing.setPassword(encoder.encode(password)); users.save(existing);
     }
     jobs.findAll().forEach(j -> { if(j.getApprovalStatus()==null){ j.setApprovalStatus(Job.ApprovalStatus.APPROVED); jobs.save(j); } });
   };
 }
}
