# Spring Boot
This is a basic Spring Boot application that includes a REST controller to handle HTTP GET requests.

## Adding In Memory Users

Check the updated SecurityConfig -> SecurityFilterChain with inline comments. 

### AuthenticationProvider 

The **AuthenticationProvider** indeed plays a crucial role in deciding which user repository (e.g., database, in-memory) to use by leveraging a specific **UserDetailsService**. 
By customizing the **AuthenticationProvider**, we control how and where user details are loaded and authenticated.  

### STEP 1 :

AuthenticationProvider is an interface & we decide to use DaoAuthenticationProvider implementation.
DaoAuthenticationProvider: A Spring Security-provided implementation of AuthenticationProvider that retrieves user details from a UserDetailsService and uses a PasswordEncoder to validate the password.

```java
@Bean
public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    return daoAuthenticationProvider;
}
```

### STEP 2 :

***Password Encoder:***

NoOpPasswordEncoder.getInstance(): This is a password encoder that performs no encoding and is deprecated in most cases because it stores passwords in plain text, which is insecure. However, it can be useful for simple or test setups where security isn't a concern.

***Setting the UserDetailsService:***

setUserDetailsService(new InMemoryUserDetailsManager()): The DaoAuthenticationProvider is configured to use the InMemoryUserDetailsManager for loading user details during authentication.

```java
@Bean
public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
    daoAuthenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager());

    return daoAuthenticationProvider;
}
```

We have not added any users to the InMemoryUserDetailsManager / UserDetailsService yet. Time to add.

### STEP 3 :

InMemoryUserDetailsManager constructor takes Collection<UserDetails>.
UserDetails is an interface & we decide to create instances of org.springframework.security.core.userdetails.User 
as it can be initialized by creating a simple name, password & empty list of Authorities.

```java
@Bean
public AuthenticationProvider authenticationProvider() {
    List<UserDetails> users = new ArrayList<>();

    for (String userName: Arrays.asList("a", "b")) {
    users.add(new User(userName, userName + "@123", List.of()));
    }

    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
    daoAuthenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager(users));

    return daoAuthenticationProvider;
}
```

## Summary
This code snippet creates a simple in-memory authentication mechanism using Spring Security:

### 1. Users: 
Two users ("a" and "b") are defined, each with a corresponding password ("a@123" and "b@123"), but without any roles or authorities.
### 2. In-Memory Storage: 
These users are stored in memory using InMemoryUserDetailsManager.
### 3. Authentication Provider: 
The DaoAuthenticationProvider is configured to use this in-memory user store and a no-op password encoder (no encoding).
### 4. Usage: 
This AuthenticationProvider is used by Spring Security to authenticate users based on the predefined usernames and passwords.

This setup is ideal for quick testing or simple applications where you don't need persistent user data storage. However, for production systems, you would typically use a more secure password encoder and store user details in a database.


# Docker Handy Commands

#### Build image
```
docker build -t auth-api:1.0.0 .
```

#### Run MYSQL container
```
docker run -d -e MYSQL_ROOT_PASSWORD=rootroot -e MYSQL_DATABASE=authdb --name mysqldb -p 3307:3306 mysql:8.0
```

#### Run container
```
docker run -p 8000:8080 auth-api:1.0.0
```

Publish a container's port(s) to the host


### Adding different profiles to Spring Boot app in Intellij

#### Create profile-specific property files:
In your project's "src/main/resources" folder, create files like "application-dev.properties" for development and "application-prod.properties" for production.

#### Making this profile specific property files accessible to Intellij :
* Go to "Run" > "Edit Configurations"
* Select your Spring Boot application configuration.
* In the "Modify options" > "Add VM options" field, add the following to specify the active profile:

```
-Dspring.profiles.active=dev
```
