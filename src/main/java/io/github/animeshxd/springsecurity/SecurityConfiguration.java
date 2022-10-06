package io.github.animeshxd.springsecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.animeshxd.springsecurity.filters.JwtAuthenticationFilter;
import io.github.animeshxd.springsecurity.filters.JwtAuthorizationFilter;
import io.github.animeshxd.springsecurity.services.CustomUserDetailsService;
import io.github.animeshxd.springsecurity.util.JwtUtils;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.Key;
import java.util.Map;

@Configuration
public class SecurityConfiguration {

    @Autowired
    public ApplicationContext applicationContext;

    @Autowired
    public UserDetailsService userDetailsService;

    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Bean
    public Key secretKey(){
        return SECRET_KEY;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        var provider = new DaoAuthenticationProvider();
               provider.setUserDetailsService(new CustomUserDetailsService());
               provider.setPasswordEncoder(passwordEncoder());
               return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests(auth -> auth
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/user").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
        );
        //http.addFilter(new JwtAuthenticationFilter(SECRET_KEY, applicationContext.getBean(AuthenticationManager.class))); // check io.github.animeshxd.springsecurity.services.JwtLoginController
        http.addFilterBefore(new JwtAuthorizationFilter(new JwtUtils(SECRET_KEY), userDetailsService).setAllowedUrls("/login", "/"), UsernamePasswordAuthenticationFilter.class);

        // exception handle
        http.exceptionHandling().accessDeniedHandler(
                (request, response, accessDeniedException) -> new ObjectMapper().writeValue(
                        response.getOutputStream(), Map.of("error", "access forbidden")
                )
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
