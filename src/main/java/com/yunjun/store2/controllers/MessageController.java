package com.yunjun.store2.controllers;

import com.yunjun.store2.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @RequestMapping("/message")
    public Message message() {
        return new Message("Hello Spring Boot!");
    }
}
