package io.github.animeshxd.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import io.github.animeshxd.springsecurity.models.CustomUser;
import io.github.animeshxd.springsecurity.services.CustomUserDetailsManager;

@Configuration
public class SecurityConfiguration {

    
    private CustomUserDetailsManager userDetailsManager;

    @Autowired
    public SecurityConfiguration(CustomUserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;

    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        userDetailsManager.createUser(
            new CustomUser()
                    .setUsername("user")
                    .setPassword("user")
                    .setPasswordEncoder(passwordEncoder()::encode)
                    .setRoles("USER")
        );
        userDetailsManager.createUser(
            new CustomUser()
                    .setUsername("admin")
                    .setPassword("admin")
                    .setPasswordEncoder(passwordEncoder()::encode)
                    .setRoles("ADMIN")
        );
        
        var provider =  new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsManager);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeRequests( autz -> autz
                .antMatchers("/").permitAll()
                .antMatchers("/user").hasAnyRole("USER","ADMIN")
                .antMatchers("/admin").hasRole("ADMIN")
            )
            .formLogin();
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
