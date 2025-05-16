package com.yunjun.store2.controllers;

import com.yunjun.store2.entities.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/message")
@Tag(name = "message", description = "Message API, play around for Restful API")
public class MessageController {

    @GetMapping("/hello")
    @Operation(summary = "Say hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/greeting")
    @Operation(summary = "Get a message")
    public Message message() {
        return new Message("Hello Spring Boot!");
    }
}
