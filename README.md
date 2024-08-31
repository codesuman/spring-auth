# Spring Boot
This is a basic Spring Boot application that includes a REST controller to handle HTTP GET requests.

## Adding spring-boot-starter-security

Adding spring-boot-starter-security to your Spring Boot web project enforces security by default, requiring users to authenticate before accessing any endpoint. You get a basic authentication mechanism with a generated password and a default login page. If no security configurations are provided, everything is secured by default.

### 1. Automatic Basic Authentication
   Spring Security will automatically configure Basic Authentication.
   Any request to your application's endpoints will require authentication.
### 2. Default Login Page
   If you run the application and try to access your simple controller (e.g., /hello), you will be redirected to a default login page provided by Spring Security.
   You can log in using the default credentials:
   Username: user
   Password: A randomly generated password is printed in the console when the application starts. It looks something like this:
   ```
   Using generated security password: 1a2345b6-1a23-1a23-1a23-1a2345b6789d
   ```
### 3. Locked Down Endpoints
   All endpoints in your application are secured by default.
   If you try to access any endpoint without authentication, you’ll either be prompted with a login page (for web apps) or receive a 401 Unauthorized response (for API endpoints).
### 4. Accessing Your Simple Controller
   If you have a simple controller like this:
   ```java
   @RestController
   public class HelloController {
        @GetMapping("/hello")
        public String hello() {
            return "Hello, World!";
        }
   }
   ```
   After adding Spring Security, attempting to access /hello in a browser or via a tool like Postman will prompt for credentials.
### 5. Authorization
   By default, once authenticated, any user can access any endpoint. However, this behavior can be customized by configuring authorization rules.


## Overriding default username & password

```
spring.security.user.name=user
spring.security.user.password=u@123
```