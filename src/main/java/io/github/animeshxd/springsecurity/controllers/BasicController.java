package io.github.animeshxd.springsecurity.controllers;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
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
    @GetMapping("/info")
    public ResponseEntity<?> info(Principal principal){
       
       return ResponseEntity.ok(principal);
    }
}
