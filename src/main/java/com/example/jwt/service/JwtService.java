package com.example.jwt.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtService {

    private static String secretKey = "java17SpringBootJWTTokenSecretKey";

    // 토큰 생성
    public String create(Map<String, Object> claims, LocalDateTime expireAt) {
        var key = Keys.hmacShaKeyFor(this.secretKey.getBytes());
        var _expireAt = Date.from(expireAt.atZone(ZoneId.systemDefault()).toInstant()); // LocalDateTime -> Date 형식으로 변경

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256) // key를 넘겨줘야 함.
                .setClaims(claims)
                .setExpiration(_expireAt)
                .compact();
    }

    // 토큰 검증
    public void validation(String token) {
        var key = Keys.hmacShaKeyFor(this.secretKey.getBytes());

        var parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        try {
            var result = parser.parseClaimsJws(token);

            result.getBody().entrySet().forEach(value -> {
                log.info("key: {}, value: {}", value.getKey(), value.getValue());
            });
        } catch (Exception e) {
            if (e instanceof SignatureException) { // 토큰이 변조된 경우
                throw new RuntimeException("JWT Token이 변조 되었음");
            } else if (e instanceof ExpiredJwtException) { // 토큰이 만료된 경우
                throw new RuntimeException("JWT Token 만료");
            } else {
                throw new RuntimeException("JWT Token 알 수 없는 예외 발생");
            }
        }

    }
}
