package com.yunjun.store2;

import com.yunjun.store2.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Store2Application {

    public static void main(String[] args) {

        var context = SpringApplication.run(Store2Application.class, args);
        var userService = context.getBean(UserService.class);
//        userService.showTransactionalScope();
//        userService.showRelatedEntities();
        userService.showPersistentState();
    }

}
