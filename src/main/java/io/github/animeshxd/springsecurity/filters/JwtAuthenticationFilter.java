package io.github.animeshxd.springsecurity.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final Key SECRET_KEY;
    private final AuthenticationManager authenticationManager;


    public JwtAuthenticationFilter(Key secret_key, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        SECRET_KEY = secret_key;
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        var username = request.getParameter(getUsernameParameter());
        var password = request.getParameter(getPasswordParameter());
        var userPassAuthToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(userPassAuthToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String jws = Jwts.builder().signWith(SECRET_KEY)
                        .setSubject(user.getUsername())
                        .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                        .setIssuedAt(new Date()).compact();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, String> json = new HashMap<>();
        json.put("token", jws);
        new ObjectMapper().writeValue(response.getOutputStream(), json);
    }

}
