package com.jwt.verification;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class Controller1 {
    @GetMapping("/sayHello")
    public String sayHello(HttpServletRequest req){
        return "Hello";
    }
}

