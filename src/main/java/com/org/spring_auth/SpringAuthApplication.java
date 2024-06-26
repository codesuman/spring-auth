package com.org.spring_auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@SpringBootApplication
public class SpringAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAuthApplication.class, args);
	}

}

@RestController
class BasicController {
	@GetMapping("/hello")
	public ResponseEntity<String> sayHello() {
		return ResponseEntity.ok("Hello World");
	}
}

@Component
@Slf4j
class LoggingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		Collections.list(request.getHeaderNames())
				.forEach(headerName -> {
					log.info("Header : {}={}", headerName, request.getHeader(headerName));
				});

		filterChain.doFilter(request, response);
	}
}