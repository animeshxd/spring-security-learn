package io.github.animeshxd.springsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    
    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails user = User
            .withDefaultPasswordEncoder()
            .username("user")
            .password("user")
            .roles("USER")
            .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("admin")
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeRequests(autz -> autz
            .antMatchers("/").permitAll()
            .antMatchers("/admin").hasAnyRole("ADMIN")
            .antMatchers("/user").hasAnyRole("ADMIN", "USER")
        ).formLogin();
        return http.build();
    }

}
