package com.jobportal.security;
import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service; import javax.crypto.SecretKey; import java.nio.charset.StandardCharsets; import java.util.*; import java.util.Date;
@Service public class JwtService {
 @Value("${jwt.secret}") private String secret; @Value("${jwt.access-token-expiration}") private long accessMs; @Value("${jwt.refresh-token-expiration}") private long refreshMs;
 private SecretKey key(){return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));}
 public String access(String email,String role){return build(email,role,accessMs,"access");}
 public String refresh(String email,String role){return build(email,role,refreshMs,"refresh");}
 private String build(String email,String role,long ttl,String type){Date now=new Date();return Jwts.builder().subject(email).claim("role",role).claim("type",type).issuedAt(now).expiration(new Date(now.getTime()+ttl)).signWith(key()).compact();}
 public Claims claims(String token){return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();}
 public String email(String token){return claims(token).getSubject();} public String role(String token){return claims(token).get("role",String.class);} public String type(String token){return claims(token).get("type",String.class);}
}
