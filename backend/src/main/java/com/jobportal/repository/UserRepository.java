package com.jobportal.repository;
import com.jobportal.entity.User; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface UserRepository extends JpaRepository<User,Long>{ Optional<User> findByEmail(String email); long countByRole(User.Role role); boolean existsByEmail(String email); }
