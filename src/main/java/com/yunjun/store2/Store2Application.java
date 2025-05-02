package com.yunjun.store2;

import entities.Address;
import entities.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Store2Application {

    public static void main(String[] args) {

        SpringApplication.run(Store2Application.class, args);
        User user = User.builder()
                .name("test")
                .email("test@test.com")
                .password("123456")
                .build();
        Address address = Address.builder()
                .street("street")
                .city("city")
                .state("state")
                .zipcode("zipcode")
                .build();
        user.addAddress(address);

        System.out.println(user);
    }

}
