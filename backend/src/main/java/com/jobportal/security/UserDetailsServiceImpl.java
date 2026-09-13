package com.jobportal.security;
import com.jobportal.entity.User;
import com.jobportal.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 private final UserRepository repo;
 public UserDetailsServiceImpl(UserRepository repo){this.repo=repo;}
 @Override public UserDetails loadUserByUsername(String email)throws UsernameNotFoundException{
  User u=repo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
  return org.springframework.security.core.userdetails.User.withUsername(u.getEmail()).password(u.getPassword()).roles(u.getRole().name()).build();
 }
}
