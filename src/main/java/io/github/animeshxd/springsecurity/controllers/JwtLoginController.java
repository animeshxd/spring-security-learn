package io.github.animeshxd.springsecurity.controllers;

import io.github.animeshxd.springsecurity.models.AuthenticationRequest;
import io.github.animeshxd.springsecurity.models.AuthenticationResponse;
import io.github.animeshxd.springsecurity.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtLoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request){
        try {
            var userPassAuthToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
//            System.out.println(1);
            Authentication authenticate = authenticationManager.authenticate(userPassAuthToken);
            String jws = jwtUtils.generateToken((User) authenticate.getPrincipal());
            return ResponseEntity.ok().body(new AuthenticationResponse(jws));
        }catch (BadCredentialsException | LockedException | DisabledException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bad Credentials");
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

    }
}
