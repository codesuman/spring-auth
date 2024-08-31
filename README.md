# Spring Boot
This is a basic Spring Boot application that includes a REST controller to handle HTTP GET requests.

## Adding spring-boot-starter-security

Adding spring-boot-starter-security to your Spring Boot web project enforces security by default, requiring users to authenticate before accessing any endpoint. You get a basic authentication mechanism with a generated password and a default login page. If no security configurations are provided, everything is secured by default.

### Why Adding a Custom SecurityFilterChain Disables the Default Login Page
When you define your own SecurityFilterChain bean, Spring Security interprets this as an indication that you want to take full control over the security configuration. As a result, it disables the auto-configured defaults, including the default login page.

Your configuration:

```java
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.build();
    }
}
```

In this setup:

No Security Rules Defined: Since you're not specifying any security rules, Spring Security doesn't apply its default behaviors.
Default Login Page Disabled: Without explicit configuration, the default login page is not enabled, and no authentication mechanisms are applied.