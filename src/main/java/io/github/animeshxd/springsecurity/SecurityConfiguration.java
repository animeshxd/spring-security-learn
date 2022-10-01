package io.github.animeshxd.springsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

@Configuration
public class SecurityConfiguration {
    
    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails user = User
            .builder()
            .username("user")
            .password("user")
            .passwordEncoder(passwordEncoder()::encode)
            .roles("USER")
            .build();
        UserDetails admin = User.builder()
            .username("admin")
            .password("admin")
            .passwordEncoder(passwordEncoder()::encode)
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
            .antMatchers("/success").hasAnyRole("ADMIN", "USER")
        ).formLogin(form -> form
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/success", true)
                .usernameParameter("user")
                .passwordParameter("pass")
        ).rememberMe(rememberMe -> rememberMe
                .tokenValiditySeconds(5)
                .key("some secret key")
                .rememberMeParameter("remember")
        ).logout(logout -> logout
                .logoutUrl("/logout")
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", "remember-me")
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
        // return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
