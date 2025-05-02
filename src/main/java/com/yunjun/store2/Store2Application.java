package com.yunjun.store2;

import com.yunjun.store2.entities.User;
import com.yunjun.store2.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Store2Application {

    public static void main(String[] args) {

        var context = SpringApplication.run(Store2Application.class, args);
        User user = User.builder()
                .name("John")
                .email("john@example.com")
                .password("test")
                .build();
        var userRepository = context.getBean(UserRepository.class);
        userRepository.save(user);
        userRepository.findAll().forEach(u -> {
                    System.out.println(u.getEmail());
                });
        userRepository.delete(user);
    }

}
