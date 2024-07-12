# Spring Boot with Spring Security

This project demonstrates the use of Spring Security in a Spring Boot application, including custom authentication with `AuthenticationManager`, `AuthenticationProvider`, and `UserDetailsService`.

In this exercise, we will focus on implementing the UserDetailsService by loading user details into a `HashMap` for simplicity. This approach allows us to concentrate on understanding the authentication flow without the complexity of connecting to a database through `UserRepository`.


## Authentication Flow

The core components involved in the authentication process are:

- **`Filters`**: Intercept HTTP requests and perform security-related operations.
- **`AuthenticationManager`**: Coordinates the authentication process.
- **`AuthenticationProvider`**: Performs the actual authentication logic.
- **`UserDetailsService`**: Retrieves user-related data.

### Filters

Spring Security uses filters to intercept HTTP requests and perform various security-related tasks.

Common filters include:

- **`UsernamePasswordAuthenticationFilter`**: Captures login credentials.
- **`BasicAuthenticationFilter`**: Handles HTTP Basic Authentication.
- **`CsrfFilter`**: Protects against CSRF attacks.
- **`JwtFilter`** (custom): Validates JWT tokens.

Custom filters can be added to modify or extend the default behavior.


### AuthenticationManager

This is the central point for handling authentication in Spring Security. It delegates authentication requests to a list of `AuthenticationProvider` instances.
`AuthenticationManager` can be configured with multiple `AuthenticationProviders`, each handling a specific type of authentication (e.g., username/password, token-based, etc.).


```java
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
}
```

### Authentication Provider

Each `AuthenticationProvider` is responsible for performing a specific authentication logic. For example, `DaoAuthenticationProvider` uses UserDetailsService to fetch user details and verify credentials.
If an `AuthenticationProvider` cannot authenticate the request, it passes the request to the next provider in the chain.

The most common provider is `DaoAuthenticationProvider`.


```java
@Bean
public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(customUserDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
}
```

### UserDetailsService

This interface is used to retrieve user-specific data. It has a single method loadUserByUsername which returns a UserDetails object.
UserDetails contains necessary information about the user such as username, password, and authorities (roles).

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Map<String, String> usersMap = new HashMap<>();
        usersMap.put("ricky", passwordEncoder.encode("123"));
        usersMap.put("martin", passwordEncoder.encode("456"));

        if (usersMap.containsKey(username)) {
            return new User(username, usersMap.get(username), new ArrayList<>());
        }

        throw new UsernameNotFoundException(username + " not found.");
    }

}
```

### Security Config

The `SecurityConfig` class initializes all the building blocks of Spring Security for a Spring Boot application. It sets up essential components such as password encoding, authentication providers, and security filters to enforce authentication and authorization policies.

Annotations
- **`@Configuration`**: Indicates that the class can be used by the Spring IoC container as a source of bean definitions.
- **`@EnableWebSecurity`**: Enables Spring Security's web security support and provides the Spring MVC integration.
- **`@EnableMethodSecurity`**: Enables method-level security annotations like @PreAuthorize and @Secured.

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    /**
     * Defines a UserDetailsService bean that uses the custom CustomUserDetailsService implementation.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }
    /**
     * Defines a PasswordEncoder bean that uses BCryptPasswordEncoder to encode passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures a DaoAuthenticationProvider bean with the custom UserDetailsService and PasswordEncoder.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        /**If we don't provide AuthenticationProvider Bean then AuthenticationManager will be set to null during app initialization. Comment this method out, run the app & check the logs.
        LOG : No authenticationProviders and no parentAuthenticationManager defined. Returning null.
        */
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    /**
     * Provides an AuthenticationManager bean, which is central to the authentication process. It is configured with the help of AuthenticationConfiguration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures the security filter chain, specifying which endpoints are publicly accessible and which require authentication.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF else face this issue - https://stackoverflow.com/q/78719300/
            .authorizeHttpRequests(req ->
                req.requestMatchers(
                        "/auth/**",
                        "/css/**.css",
                        "/js/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
```

Here is the code of our `AuthController`:

```java
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } catch (Exception e){
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }
}

@Data
@NoArgsConstructor
class AuthRequest {
    private String username;
    private String password;
}
```

The `authenticationManager.authenticate()` method is a crucial part of the authentication process in Spring Security. Here's a detailed breakdown of what happens during this call:

#### 1. Authentication Token Creation :
When you call authenticationManager.authenticate(), you typically pass it an instance of Authentication (e.g., UsernamePasswordAuthenticationToken) which contains the user's credentials (username and password).

#### 2. Provider Delegation :
The AuthenticationManager is responsible for managing a list of AuthenticationProviders. Each AuthenticationProvider knows how to perform a specific type of authentication. When you call authenticate(), the AuthenticationManager iterates through its list of AuthenticationProviders, attempting to authenticate the token.

#### 3. Authentication Provider Attempts Authentication :
Each AuthenticationProvider tries to authenticate the provided Authentication token. If an AuthenticationProvider supports the token type, it will attempt to authenticate the user.

For example, the DaoAuthenticationProvider uses a UserDetailsService to load the user's details from a database or another persistent store and then compares the provided password with the stored one using a PasswordEncoder.

#### 4. Successful Authentication
If an AuthenticationProvider successfully authenticates the token, it returns a fully populated Authentication object. This object includes details about the authenticated user, such as authorities/roles.

#### 5. Authentication Failure
If no AuthenticationProvider can authenticate the token, an AuthenticationException is thrown. This indicates that the authentication process has failed.

#### 6. Returning the Authentication Object
If authentication is successful, the AuthenticationManager returns the fully populated Authentication object to the caller (in this case, your AuthController).


---------------
### Step-by-Step Example Flow
#### 1. Receive Request:
A POST request to /auth/login with JSON body {"username": "ricky", "password": "123"}.
#### 2. Create Token:
The controller creates a UsernamePasswordAuthenticationToken with the username and password.
#### 3. Authenticate:
authenticationManager.authenticate() is called with this token.
#### 4. Provider Loop:
The AuthenticationManager iterates through its AuthenticationProviders.
#### 5. UserDetailsService:
The DaoAuthenticationProvider (or your custom provider) uses UserDetailsService to load user details.
#### 6. Password Check:
The DaoAuthenticationProvider checks the password using the configured PasswordEncoder.
#### 7. Success:
If the password matches, a fully populated Authentication object is returned.
#### 8. Return Response:
The controller returns a success message.