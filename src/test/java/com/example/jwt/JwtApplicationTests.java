package com.example.jwt;

import com.example.jwt.service.JwtService;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Test
	void contextLoads() {
	}

	@Test
	void tokenCreate() {
		var claims = new HashMap<String, Object>();
		claims.put("user_id", 304);

		var expiredAt = LocalDateTime.now().plusSeconds(20);

		var jwtToken = jwtService.create(claims, expiredAt);

		System.out.println(jwtToken);
	}

	@Test
	void tokenValidation() {
		var token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjozMDQsImV4cCI6MTcwNjA3MzE3Mn0.JUAhNqWlTVcInxioTO6j5RVKeUUF4tlkqZePdxXJUw";

		jwtService.validation(token);
	}

}
