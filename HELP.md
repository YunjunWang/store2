# Getting Started
Spring Boot learning project following codewithmosh.com course.

## JDK - Java Development Kit
* IntelliJ Ultimate integrated capability of search and download JDK
* Oracle website to download and install
* Mac: ```brew update```, ```brew install java```
### Debugging Tool
### Standard Library
### Compiler

### JRE - Java Runtime Environment
Supports agnostic Java program runtime environment, compiling and executing Java programs in any environment.
#### JVM - Java Virtual Machine

## Build Tool
### Maven - More widely used and has been around longer.
* IntelliJ Ultimate integrated Maven tools
* Maven installation: https://maven.apache.org/install.html for other IDE
* Mac: ```brew update```, ```brew install maven```
* maven central repository: https://central.sonatype.com/artifact/org.apache.maven.plugins/maven-source-plugin
* pom.xml for dependencies
### Gradle - More modern, concise, and optimized for performance
    
## Spring
* Spring Core: Object
* Spring Web: communicate with FrontEnd and DB
* Spring Security: 
* Spring AOP: Aspect Oriented Programming, 
* Spring Data: ?.
* Spring Cloud:

## SpringBoot
* Spring Boot is a framework that simplifies Java application development by
handling configuration, dependency management, and embedded servers.
* It allows us to quickly build production-ready applications with minimal setup.
### Spring Boot Starter Web
* Spring Core
* Spring Web
* Spring log?
* Spring JSON?
* Spring Test


### Spring Core
Spring can create objects and inject them into classes automatically.
At its core is IOC Container.
* IOC Container - manages objects including 
                  creating, injection, deleting etc.(as Beans lifecycle) 
                  in our application.
* Bean - the objects managed by Spring IOC container.
* IOC - Inversion of Control
* Inverts the control of creating objects and managing out dependencies.

#### Configuring Beans
* Using annotations
    > @Component("bean_name") - for general purpose annotation, e.g. utility classes
    > @Service("bean_name") - marking classes contain business logic
    > @Repository("bean_name") - marking classes that interacts with DB
    > @Controller("bean_name") - marking classes as controllers for handling web requests
    > @Value("${your_application_property_name}") - to get application configuration properties values
* Using code - more verbose, more control, for simple scenarios use the annotations only
    > for using beans conditionally
    > for Third-party library objects which we don't have control of

#### Bean Selection Control
When multiple Beans provided it can be confusing which one to choose. 
By using the Bean selection annotations helps Spring IoC to decide which one to use.
* @Primary - can only mark one Bean as Primary
* @Qualifier("your_bean_name")

#### Externalizing Configuration
* Any configurations should be set in an externalized application.properties / application.yaml file.
* Only use the application.properties / application.yaml not both.
* Usually the application.yaml will overtake the application.properties if both exists(seems in this IntelliJ is the other way around).
* Use @Value("${your_application_configuration_property_full_path_name: you_default_value}") to get 
  required application configuration property's value. If there's no value set for this property in the application.* file
  it'll use the default value otherwise will use the value set in the application.* file.

#### Bean Scopes: @scope("scopetype")
* singleton - the default scope, only one bean instance will be created and exist in the whole application lifecycle
              useful for stateless, reusable components, e.g. services
* prototype - one bean instance created every time there's a request from the IOC container, 
              it is useful for components that holds state / used temporarily.
* request - one bean created per Http request,
            it's useful for storing request specific data in web applications
* session - one bean created per Http session

#### Bean Lifecycle Hooks
* @PostConstruct
* @PreDestroy

### Spring Web
#### Controller
#### RestController
##### Validation
* Add dependencies
  ```org.springframework.boot: spring-boot-starter-validation```

#### Service
#### ServiceImpl
#### DTO <-> Entity
#### Auto Mapping for DTO <-> Entity
* Add dependencies:
```
        <dependency>
            <artifactId>mapstruct</artifactId>
            <groupId>org.mapstruct</groupId>
            <version>1.6.2</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
            <version>3.3.4</version>
        </dependency>

       ...
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <annotationProcessorPaths>
                        ...
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>1.6.3</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
```
* create mappers under mapper directory 
```
@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
    CartItem toEntity(CartItemDto cartItemDto);
}
```
### Spring Security
#### Add dependencies for Spring Security
* go to pom.xml
* Cmd + N
* Starter
* org.springframework.boot: spring-boot-starter-security
#### Add dependencies for JWT in pom.xml
* io.jsonwebtoken as groupId
* jjwt-impl
* jjwt-jackson
* jjwt-api
#### Generate JWT token
* Create a JWT token service
* Use Jwts builder to build the token with email, iat, expiration and secret
* Return this token as a customized JwtResponse to the client
#### Managing secret
* add dependency: spring.dotenv from https://github.com/paulschwarz/spring-dotenv
* .env for actual secret but never commit into repo
* .env.example to share how to set up secret, stays in the repo
* use openssl to generate 64 bytes secret and save it in the .env file
```openssl rand -base64 64```
* use application.yml to get the secret from .env
* use @Value(${app_config_name}) to get the value inside auth service
* check token generated on jwt.io
#### Validate JWT Token


### Spring AOP

### Spring Data

### Spring Cloud


## Dev Environment Learning
### Create A New Spring Boot Project
* IntelliJ Ultimate version: new / Generator / Spring Boot
* springboot.ie/generator, download the zip file and import to your own IDE

### Spring Boot Project Structure
A Spring Boot project consists of:
* src/main/java: Contains the application code.
* src/main/resources: Stores configuration files (application.properties or
application.yml).
* pom.xml or build.gradle: Manages project dependencies using Maven or
Gradle.

### Running Spring Boot Applications
Spring Boot provides multiple ways to run an application:
* Using IntelliJ's Run button
* Running ./mvnw spring-boot:run from the terminal

### IntelliJ Shortcuts
* CMD + n inside the pom.xml file: Search / Add Dependencies for the current project
* CTRL + r: run the program
* CTRL + d: debug the program
* CMD + SHIFT + o: search a file

### Linux Command
* To list all the active processes 
```lsof -i :<port_number>```
* To kill a process 
```kill -9 <process_id>```
* To generate 64 bytes(characters) secret
```openssl rand -base64 64```

### Reference Documentation
For further reference, please consider the following sections:
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.0-M3/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.0-M3/maven-plugin/build-image.html)

### Maven Parent overrides
Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the
parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

