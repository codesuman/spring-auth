# Spring Boot
This is a basic Spring Boot application that includes a REST controller to handle HTTP GET requests.

## Main Application Class
The main application class, `SpringAuthApplication`, is annotated with `@SpringBootApplication`. This annotation combines three crucial annotations: `@Configuration`, `@EnableAutoConfiguration` and `@ComponentScan`. This setup enables Spring Boot`s auto-configuration feature and component scanning, allowing the application to automatically configure itself based on the dependencies present in the classpath.

```java
@SpringBootApplication
public class SpringAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAuthApplication.class, args);
	}

}
```

The main method is the entry point of the application. It uses SpringApplication.run to launch the application.

## REST Controller
The BasicController class is a simple REST controller that handles HTTP GET requests to the /hello endpoint.

```java
@RestController
class BasicController {
	@GetMapping("/hello")
	public ResponseEntity<String> sayHello() {
		return ResponseEntity.ok("Hello World");
	}
}
```

- **`@RestController`**: This annotation indicates that the class is a controller where every method returns a domain object instead of a view. It is a convenience annotation that combines @Controller and @ResponseBody.
- **`@GetMapping("/hello")`**: This annotation maps HTTP GET requests to the /hello endpoint to the sayHello method.
- **`ResponseEntity.ok("Hello World")`**: This returns a response with the HTTP status 200 (OK) and the body containing the string "Hello World".
When a GET request is made to the /hello endpoint, the server responds with "Hello World".

This basic setup demonstrates the core structure of a Spring Boot application with a simple REST endpoint.