package io.github.animeshxd.springsecurity.models;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "users")
public class CustomUser {

    @Id
    private String username;
    private String password;
    private boolean enabled = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "authoroties", joinColumns = @JoinColumn(name="username"))
    private List<String> authorities;

    
    public CustomUser(String username, String password, List<String> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }
    public CustomUser() {}


    @Transient
    public UserDetails build(){
        return User.withUsername(username)
                    .password(password)
                    .authorities(authorities.stream().map(SimpleGrantedAuthority::new).toList())
                    .build();
    }

    public String getUsername() {
        return username;
    }

    public CustomUser setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public CustomUser setPassword(String password) {
        this.password = password;
        
        return this;
    }

    public CustomUser setPasswordEncoder(Function<String, String> encoder){
        password = encoder.apply(password);
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public CustomUser setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public CustomUser setAuthorities(String ...authorities) {
        this.authorities = List.of(authorities);
        return this;
    }

    public CustomUser setRoles(String ...roles){
        this.authorities = Arrays.stream(roles).map(t -> "ROLE_"+t).toList();
        return this;
    }
    
    
}
