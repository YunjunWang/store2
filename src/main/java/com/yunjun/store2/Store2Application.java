package com.yunjun.store2;

import entities.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class Store2Application {

    public static void main(String[] args) {

        SpringApplication.run(Store2Application.class, args);
        User user = User.builder()
                .name("test")
                .email("test@test.com")
                .password("123456")
                .build();
/*      User entity can't add address and tags the same time, get StackOverflow, how to solve it?
        Address address = Address.builder()
                .street("street")
                .city("city")
                .state("state")
                .zipcode("zipcode")
                .build();
        user.addAddress(address);*/

        user.addTag("tag1");
        Profile profile = Profile.builder().bio("bio").build();
        user.addProfile(profile);

        System.out.println(user);

        Category category1 = new Category("category1");
        Product product1 = Product.builder()
                            .name("product1")
                            .price(BigDecimal.valueOf(100.00))
                            .category(category1)
                            .build();
        System.out.println(product1);
    }

}
