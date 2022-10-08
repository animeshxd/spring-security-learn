package io.github.animeshxd.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import io.github.animeshxd.springsecurity.services.CustomOuth2UserService;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeRequests(
            authorization -> authorization
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
        );

        http.oauth2Login().userInfoEndpoint().userService(oAuth2UserService);
        http.formLogin();
        return http.build();


    }       
    @Autowired
    CustomOuth2UserService oAuth2UserService;
}
