package io.github.animeshxd.springsecurity.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.animeshxd.springsecurity.models.CustomUser;
import io.github.animeshxd.springsecurity.repositories.CustomUserDetailsRepository;

@Service
public class CustomUserDetailsManager implements UserDetailsService {

    private CustomUserDetailsRepository repository;

    @Autowired
    public CustomUserDetailsManager(CustomUserDetailsRepository repository) {
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException(username + " not found"))
            .build();
    }

    public CustomUser createUser(CustomUser user){
        return repository.save(user);
    }
}
