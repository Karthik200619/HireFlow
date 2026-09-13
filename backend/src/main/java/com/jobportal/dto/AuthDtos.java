package com.jobportal.dto;

import jakarta.validation.constraints.*;

public class AuthDtos {
  public record LoginRequest(@Email @NotBlank String email,@NotBlank String password){}
  public record RegisterRequest(@NotBlank String name,@Email @NotBlank String email,@NotBlank String phoneNumber,@Size(min=6) String password,Integer age,Double experience,String address,String qualification,Long companyId){}
  public record TokenRequest(@NotBlank String refreshToken){}
  public record AuthResponse(String accessToken,String refreshToken,String tokenType,Long userId,String name,String email,String role){ public static AuthResponse of(String a,String r,com.jobportal.entity.User u){return new AuthResponse(a,r,"Bearer",u.getId(),u.getName(),u.getEmail(),u.getRole().name());}}
}
