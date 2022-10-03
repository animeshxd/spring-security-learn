package io.github.animeshxd.springsecurity.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {
    
    @GetMapping("/")
    public String root(){
        return "Welcome everyone";
    }

    @GetMapping("/user")
    public String user(){
        return "Welcome User";
    }
    
    @GetMapping("/admin")
    public String admin(){
        return "Welcome Admin";
    }
}
