package com.org.spring_auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@SpringBootApplication
public class SpringAuthApplication {

	/**
	 *
	 * Good Reads :
	 * https://backendstory.com/spring-security-authentication-architecture-explained-in-depth/
	 * https://medium.com/@tericcabrel/implement-jwt-authentication-in-a-spring-boot-3-application-5839e4fd8fac
	 * https://medium.com/javarevisited/spring-framework-filter-vs-dispatcher-servlet-vs-interceptor-vs-controller-745aa34b08d8
	 */
	public static void main(String[] args) {
		SpringApplication.run(SpringAuthApplication.class, args);
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