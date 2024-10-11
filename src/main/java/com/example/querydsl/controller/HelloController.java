package com.example.querydsl.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String getMethodName() {
        return "<h1>Hello!!</h1>";
    }

}
