package io.github.animeshxd.springsecurity.services;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    Map<String, User> users = new HashMap<>();

    public CustomUserDetailsService() {
        users.put("user", new User("user", "user", List.of(new SimpleGrantedAuthority("ROLE_USER"))));
        users.put("admin", new User("admin", "admin", List.of(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN"))));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println(2);
        User user = users.get(username);
        if (user == null){
            System.out.printf("user not found %s", username);
            throw new UsernameNotFoundException("user not found %s".formatted(username));
        }
        return new User(user.getUsername(), user.getPassword(), user.getAuthorities()); // new User() is required as someone will set password null
    }

}
