package com.jobportal.security;
import org.springframework.scheduling.annotation.Scheduled; import org.springframework.stereotype.Component; import java.time.*; import java.util.concurrent.*;
@Component public class TokenBlacklist { private final ConcurrentMap<String,Instant> map=new ConcurrentHashMap<>(); public void add(String token,Instant expiry){map.put(token,expiry);} public boolean contains(String token){return map.containsKey(token);} @Scheduled(fixedRate=3600000) public void cleanup(){Instant n=Instant.now();map.entrySet().removeIf(e->e.getValue().isBefore(n));} }
