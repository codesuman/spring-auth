package com.org.spring_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
